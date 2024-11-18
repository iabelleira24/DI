import tkinter as tk
from tkinter import simpledialog, messagebox
from PIL import Image, ImageTk
import random
import time


class MainMenu:
    def __init__(self, root, start_game_callback, show_stats_callback, quit_callback):
        self.root = root
        self.start_game_callback = start_game_callback
        self.show_stats_callback = show_stats_callback
        self.quit_callback = quit_callback

        self.frame = tk.Frame(self.root)
        self.frame.pack(padx=20, pady=20)

        self.title_label = tk.Label(self.frame, text="Juego de Memoria", font=("Arial", 24))
        self.title_label.pack(pady=20)

        self.start_button = tk.Button(self.frame, text="Jugar", command=self.start_game_callback)
        self.start_button.pack(pady=10)

        self.stats_button = tk.Button(self.frame, text="Estadísticas", command=self.show_stats_callback)
        self.stats_button.pack(pady=10)

        self.quit_button = tk.Button(self.frame, text="Salir", command=self.quit_callback)
        self.quit_button.pack(pady=10)

    def ask_player_name(self):
        name = simpledialog.askstring("Nombre del jugador", "¿Cuál es tu nombre?", parent=self.root)
        return name


class GameView:
    def __init__(self, root, game_model, game_controller):
        self.root = root
        self.game_model = game_model
        self.game_controller = game_controller

        self.frame = tk.Frame(self.root)
        self.frame.pack(padx=10, pady=10)

        self.cards_flipped = []
        self.buttons = []
        self.matched_cards = []

        self.timer_label = tk.Label(self.frame, text="Tiempo: 0s", font=("Arial", 14))
        self.timer_label.grid(row=10, column=0, columnspan=2, pady=10)

        self.moves_label = tk.Label(self.frame, text="Movimientos: 0", font=("Arial", 14))
        self.moves_label.grid(row=10, column=2, columnspan=2, pady=10)

    def create_board(self):
        num_cards = len(self.game_model.board)
        rows = self.game_model.rows
        cols = self.game_model.cols
        index = 0

        for row in range(rows):
            for col in range(cols):
                if index < num_cards:
                    card_button = tk.Button(self.frame, text="?", width=10, height=5,
                                            command=lambda index=index: self.game_controller.on_card_click(index))
                    card_button.grid(row=row, column=col, padx=5, pady=5, sticky="nsew")
                    self.buttons.append(card_button)
                    index += 1

        for i in range(rows):
            self.frame.grid_rowconfigure(i, weight=1, uniform="equal")
        for j in range(cols):
            self.frame.grid_columnconfigure(j, weight=1, uniform="equal")

    def update_board(self, selected, matched=False):
        for index, card_value in selected:
            if matched:
                self.buttons[index].config(state="disabled", relief="sunken")
                self.matched_cards.append(index)
            else:
                self.buttons[index].config(text=f"{card_value + 1}", relief="sunken")

    def reset_cards(self, card1_index, card2_index):
        self.buttons[card1_index].config(text="?", relief="raised")
        self.buttons[card2_index].config(text="?", relief="raised")

    def update_timer(self, time):
        self.timer_label.config(text=f"Tiempo: {time}s")

    def update_moves(self, moves):
        self.moves_label.config(text=f"Movimientos: {moves}")


class GameModel:
    def __init__(self, rows=4, cols=4):
        self.rows = rows
        self.cols = cols
        self.board = []
        self.reset_board()

    def reset_board(self):
        total_cards = self.rows * self.cols
        card_values = list(range(total_cards // 2)) * 2
        random.shuffle(card_values)
        self.board = card_values


class GameController:
    def __init__(self, root):
        self.root = root
        self.model = GameModel()
        self.menu = None
        self.view = None
        self.player_name = ""
        self.moves = 0
        self.timer = 0
        self.flipped_cards = []
        self.matched_pairs = 0
        self.running = False

    def start_game(self):
        if self.menu:
            self.menu.frame.destroy()
        self.model.reset_board()
        self.view = GameView(self.root, self.model, self)
        self.view.create_board()
        self.moves = 0
        self.timer = 0
        self.flipped_cards = []
        self.matched_pairs = 0
        self.running = True
        self.update_timer()

    def show_stats(self):
        messagebox.showinfo("Estadísticas", "Función no implementada.")

    def quit_game(self):
        self.root.quit()

    def on_card_click(self, index):
        if not self.running or index in self.flipped_cards:
            return

        self.flipped_cards.append(index)
        self.view.update_board([(index, self.model.board[index])])

        if len(self.flipped_cards) == 2:
            card1_index, card2_index = self.flipped_cards
            card1_value = self.model.board[card1_index]
            card2_value = self.model.board[card2_index]

            if card1_value == card2_value:
                self.view.update_board([(card1_index, card1_value), (card2_index, card2_value)], matched=True)
                self.matched_pairs += 1
            else:
                self.root.after(1000, lambda: self.view.reset_cards(card1_index, card2_index))

            self.flipped_cards = []
            self.moves += 1
            self.view.update_moves(self.moves)

            if self.matched_pairs == len(self.model.board) // 2:
                self.running = False
                messagebox.showinfo("¡Ganaste!", f"¡Felicidades, {self.player_name}! Terminaste el juego en {self.timer}s con {self.moves} movimientos.")

    def update_timer(self):
        if self.running:
            self.timer += 1
            self.view.update_timer(self.timer)
            self.root.after(1000, self.update_timer)

    def run(self):
        self.menu = MainMenu(self.root, self.start_game, self.show_stats, self.quit_game)
