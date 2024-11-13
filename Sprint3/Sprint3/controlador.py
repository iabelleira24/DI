import tkinter as tk
from tkinter import messagebox, simpledialog
from modelo import GameModel
from vista import MainMenu, GameView
import threading

class GameController:
    def __init__(self, root):
        self.root = root
        self.model = None
        self.selected = []
        self.timer_started = False
        self.player_name = None
        self.difficulty = None
        self.main_menu = MainMenu(root, self.start_game_callback, self.show_stats_callback, self.quit_callback)
        self.loading_window = None

    def show_difficulty_selection(self):

        difficulty = simpledialog.askstring("Dificultad", "Selecciona la dificultad: facil, medio, dificil", parent=self.root)
        if difficulty in ["facil", "medio", "dificil"]:
            self.difficulty = difficulty
            self.player_name = simpledialog.askstring("Nombre", "Ingresa tu nombre:", parent=self.root)
            if self.player_name:
                self.start_game(self.difficulty)
        else:
            messagebox.showerror("Error", "Dificultad no válida. Por favor elige 'facil', 'medio' o 'dificil'.")

    def start_game(self, difficulty):

        self.show_loading_window("Cargando juego...")
        self.model = GameModel(difficulty, self.player_name)
        threading.Thread(target=self.check_images_loaded).start()

    def show_loading_window(self, message):

        self.loading_window = tk.Toplevel(self.root)
        self.loading_window.title("Cargando")
        label = tk.Label(self.loading_window, text=message)
        label.pack()
        self.loading_window.grab_set()

    def check_images_loaded(self):

        self.model.load_images()
        while not self.model.images_loaded.is_set():
            self.root.after(100)
        self.loading_window.destroy()
        self.game_view = GameView(self.root, self.on_card_click, self.model.board)
        self.update_time()

    def on_card_click(self, pos):
        # Fase 4: Interacción del jugador (selección de cartas)
        if not self.timer_started:
            self.model.start_timer()
            self.update_time()
            self.timer_started = True

        if pos not in self.selected and len(self.selected) < 2:
            self.selected.append(pos)
            self.game_view.update_board(pos, self.model.reveal_card(pos))
            if len(self.selected) == 2:
                self.handle_card_selection()

    def handle_card_selection(self):
        card1, card2 = self.selected
        if self.model.check_match(card1, card2):
            self.game_view.lock_pair(card1, card2)
        else:
            self.root.after(500, lambda: self.game_view.reset_cards(card1, card2))
        self.selected = []
        self.update_move_count(self.model.increment_moves())
        self.check_game_complete()

    def update_move_count(self, moves):
        self.game_view.update_move_count(moves)

    def check_game_complete(self):

        if self.model.is_game_complete():
            messagebox.showinfo("Felicidades", f"¡Has completado el juego en {self.model.moves} movimientos!")
            self.model.save_score()
            self.return_to_main_menu()

    def return_to_main_menu(self):
        self.game_view.destroy()
        self.main_menu.show()

    def show_stats(self):
        scores = self.model.load_scores()
        stats_message = "\n".join([f"{k}: {v}" for k, v in scores.items()])
        messagebox.showinfo("Estadísticas", stats_message)

    def update_time(self):
        if self.timer_started and not self.model.is_game_complete():
            elapsed_time = self.model.get_elapsed_time()
            self.game_view.update_time(elapsed_time)
            self.root.after(1000, self.update_time)

    def start_game_callback(self):
        self.show_difficulty_selection()

    def show_stats_callback(self):
        self.show_stats()

    def quit_callback(self):
        self.root.quit()
