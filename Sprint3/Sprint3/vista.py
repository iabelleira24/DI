import tkinter as tk
from tkinter import simpledialog, Toplevel, Label

class MainMenu:
    def __init__(self, root, start_game_callback, show_stats_callback, quit_callback):
        self.root = root
        self.start_game_callback = start_game_callback
        self.show_stats_callback = show_stats_callback
        self.quit_callback = quit_callback
        self.build_menu()

    def build_menu(self):
        tk.Button(self.root, text="Jugar", command=self.start_game_callback).pack()
        tk.Button(self.root, text="Estadísticas", command=self.show_stats_callback).pack()
        tk.Button(self.root, text="Salir", command=self.quit_callback).pack()

    def ask_player_name(self):
        return simpledialog.askstring("Nombre del Jugador", "Introduce tu nombre:")

    def show_stats(self, stats):
        stats_win = Toplevel(self.root)
        stats_win.title("Estadísticas")

class GameView:
    def __init__(self, root, on_card_click_callback, update_move_count_callback, update_time_callback):
        self.root = root
        self.on_card_click_callback = on_card_click_callback
        self.update_move_count_callback = update_move_count_callback
        self.update_time_callback = update_time_callback
        self.labels = []

    def create_board(self, model):
        self.window = Toplevel(self.root)
        self.window.title("Juego de Memoria")

        for r, row in enumerate(model.board):
            for c, cell in enumerate(row):
                label = Label(self.window, image=model.hidden_image)
                label.grid(row=r, column=c)
                label.bind("<Button-1>", lambda e, pos=(r, c): self.on_card_click_callback(pos))
                self.labels.append(label)
