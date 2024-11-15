import tkinter as tk
from tkinter import simpledialog, messagebox
import threading
from PIL import Image, ImageTk
from datetime import datetime

from modelo import GameModel
from vista import GameView, MainMenu
from recursos import descargar_imagen


class GameController:
    def __init__(self, root):
        self.root = root
        self.player_name = ""
        self.difficulty = ""
        self.game_model = None
        self.game_view = None
        self.selected = []
        self.move_count = 0
        self.start_time = 0
        self.images_loaded = False
        self.image_urls = [
            # Lista de URLs corregidas
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/967985ca22b2b00b485eca2bd47295d2.png",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/OIP%20(1).jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/OIP%20(2).jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/OIP%20(3).jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/OIP%20(4).jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/OIP%20(5).jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/OIP.jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/R%20(1).png",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/R%20(2).jpg",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/R%20(2).png",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/R.png",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/png-transparent-pokemon-go-pokemon-art-academy-pikachu-drawing-pokemon-cartoon-fictional-character-pokemon.png",
            "https://github.com/iabelleira24/DI/blob/main/Sprint3/imagenes%20poke/png-transparent-pokemon-go-pokemon-x-and-y-ash-ketchum-charmander-pokemon-background-orange-cartoon-fictional-character.png"
        ]
        self.images = []

        self.main_menu = MainMenu(self.root, self.start_game_callback, self.show_stats_callback, self.quit_callback)

    def start_game_callback(self):
        self.reset_game_state()
        self.show_difficulty_selection()

    def reset_game_state(self):
        """Reiniciar el estado del juego."""
        self.game_model = None
        self.game_view = None
        self.selected = []
        self.move_count = 0
        self.start_time = 0
        self.images_loaded = False
        self.images = []

    def show_difficulty_selection(self):
        difficulty = simpledialog.askstring("Selecciona la dificultad",
                                            "Elige la dificultad: fácil, medio, difícil",
                                            parent=self.root).lower()
        if difficulty is None:
            return

        self.player_name = self.main_menu.ask_player_name()
        if self.player_name is None:
            return

        if difficulty in ["facil", "medio", "dificil"]:
            self.difficulty = difficulty
            self.game_model = GameModel(self.difficulty)
            self.show_loading_window()
            self.load_images_thread()
        else:
            messagebox.showwarning("Dificultad inválida", "Por favor elige una dificultad válida.")

    def show_loading_window(self):
        if hasattr(self, 'loading_window') and self.loading_window.winfo_exists():
            self.loading_window.destroy()

        self.loading_window = tk.Toplevel(self.root)
        self.loading_window.title("Cargando imágenes")
        self.loading_label = tk.Label(self.loading_window, text="Cargando imágenes, por favor espere...",
                                      font=("Arial", 14))
        self.loading_label.pack(padx=20, pady=20)
        self.loading_window.protocol("WM_DELETE_WINDOW", self.prevent_window_close)

    def prevent_window_close(self):
        pass

    def load_images_thread(self):
        threading.Thread(target=self.load_images).start()

    def load_images(self):
        try:
            for url in self.image_urls:
                threading.Thread(target=self.iniciar_imagen, args=(url,)).start()
        except Exception as e:
            print(f"Error al intentar cargar imágenes: {e}")

    def iniciar_imagen(self, url):
        descargar_imagen(url, self.image_downloaded_callback)

    def image_downloaded_callback(self, imagen):
        if imagen:
            self.images.append(imagen)
        else:
            print("Error al descargar una imagen.")

        if len(self.images) == len(self.image_urls):
            self.images_loaded = True
            self.check_images_loaded()

    def check_images_loaded(self):
        if len(self.images) == len(self.image_urls):
            print("Todas las imágenes están cargadas")
            self.loading_window.destroy()
            self.start_game_window()

    def start_game_window(self):
        self.root.withdraw()
        game_window = tk.Toplevel(self.root)
        game_window.title("Juego de Memoria")
        self.game_view = GameView(game_window, self.game_model, self)
        self.game_view.create_board()
        self.update_time()

    def on_card_click(self, index):
        if len(self.selected) == 2 or any(index == selected[0] for selected in self.selected):
            return

        card_value = self.game_model.board[index]
        self.selected.append((index, card_value))
        self.game_view.update_board([(index, card_value)])

        if len(self.selected) == 2:
            self.handle_card_selection()

    def handle_card_selection(self):
        card1_index, card1_value = self.selected[0]
        card2_index, card2_value = self.selected[1]

        if card1_value == card2_value:
            self.game_view.update_board(self.selected, matched=True)
            self.check_victory()
        else:
            self.root.after(1000, self.game_view.reset_cards, card1_index, card2_index)

        self.increment_move_count()
        self.selected = []

    def increment_move_count(self):
        self.move_count += 1
        self.game_view.update_moves(self.move_count)

    def update_time(self):
        if self.root.winfo_exists():
            self.start_time += 1
            self.game_view.update_timer(self.start_time)
            self.root.after(1000, self.update_time)

    def check_victory(self):
        if len(self.game_view.matched_cards) == len(self.game_model.board):
            messagebox.showinfo("¡Felicidades!", "¡Has ganado!")
            self.game_model.save_high_score(self.player_name, self.move_count, self.start_time)
            self.root.after(100, self.return_to_main_menu)

    def return_to_main_menu(self):
        self.game_view.frame.destroy()
        self.reset_game_state()
        self.root.deiconify()

    def show_stats_callback(self):
        if self.game_model is None:
            messagebox.showinfo("Estadísticas", "No hay estadísticas disponibles.")
            return

        scores = self.game_model.high_scores
        stats_message = "Ranking por dificultad:\n"

        for difficulty, score_list in scores.items():
            stats_message += f"\n{difficulty.capitalize()}:\n"
            for idx, entry in enumerate(score_list):
                stats_message += f"{idx + 1}. {entry['name']} - Movimientos: {entry['moves']}, Tiempo: {entry['time_spent']}s, Fecha: {entry['date']}\n"

        messagebox.showinfo("Estadísticas", stats_message)

    def quit_callback(self):
        self.root.quit()
