import tkinter as tk
from tkinter import simpledialog, messagebox
from PIL import Image, ImageTk

class MainMenu:
    def __init__(self, root, start_game_callback, show_stats_callback, quit_callback):
        self.root = root
        self.start_game_callback = start_game_callback
        self.show_stats_callback = show_stats_callback
        self.quit_callback = quit_callback

        # Crear el marco principal
        self.frame = tk.Frame(self.root)
        self.frame.pack(padx=20, pady=20)

        # Título del juego
        self.title_label = tk.Label(self.frame, text="Juego de Memoria", font=("Arial", 24, "bold"))
        self.title_label.pack(pady=20)

        # Botón para iniciar el juego
        self.start_button = tk.Button(self.frame, text="Jugar", font=("Arial", 14), command=self.start_game_callback)
        self.start_button.pack(pady=10)

        # Botón para mostrar las estadísticas
        self.stats_button = tk.Button(self.frame, text="Estadísticas", font=("Arial", 14), command=self.show_stats_callback)
        self.stats_button.pack(pady=10)

        # Botón para salir
        self.quit_button = tk.Button(self.frame, text="Salir", font=("Arial", 14), command=self.quit_callback)
        self.quit_button.pack(pady=10)

    def ask_player_name(self):
        """Solicitar el nombre del jugador"""
        name = simpledialog.askstring("Nombre del jugador", "¿Cuál es tu nombre?", parent=self.root)
        return name or None  # Devuelve None si se cancela


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

        # Etiqueta para el temporizador
        self.timer_label = tk.Label(self.frame, text="Tiempo: 0s", font=("Arial", 14))
        self.timer_label.grid(row=10, column=0, columnspan=2, pady=10)

        # Etiqueta para los movimientos
        self.moves_label = tk.Label(self.frame, text="Movimientos: 0", font=("Arial", 14))
        self.moves_label.grid(row=10, column=2, columnspan=2, pady=10)

    def create_board(self):
        """Crear el tablero con botones para cada carta"""
        num_cards = len(self.game_model.board)
        rows, cols = self.game_model.rows, self.game_model.cols
        index = 0

        # Crear botones organizados en un grid
        for row in range(rows):
            for col in range(cols):
                if index < num_cards:
                    card_button = tk.Button(self.frame, text="?", width=10, height=5,
                                            command=lambda idx=index: self.game_controller.on_card_click(idx))
                    card_button.grid(row=row, column=col, padx=5, pady=5, sticky="nsew")
                    self.buttons.append(card_button)
                    index += 1

        # Configuración del tamaño de las filas y columnas
        for i in range(rows):
            self.frame.grid_rowconfigure(i, weight=1, uniform="row")
        for j in range(cols):
            self.frame.grid_columnconfigure(j, weight=1, uniform="col")

    def update_board(self, selected, matched=False):
        """Actualizar el estado del tablero"""
        for index, card_value in selected:
            if matched:
                self.buttons[index].config(state="disabled", relief="sunken")
                self.matched_cards.append(index)
            else:
                # Mostrar la imagen si está disponible
                if self.game_controller.images and card_value < len(self.game_controller.images):
                    try:
                        img = self.game_controller.images[card_value].resize((100, 100))
                        img_tk = ImageTk.PhotoImage(img)
                        self.buttons[index].config(image=img_tk, text="", relief="sunken")
                        self.buttons[index].image = img_tk  # Evitar que la imagen sea recolectada por el GC
                    except Exception as e:
                        print(f"Error mostrando imagen: {e}")
                        self.buttons[index].config(text=f"Card {card_value + 1}", relief="sunken")
                else:
                    self.buttons[index].config(text=f"Card {card_value + 1}", relief="sunken")

    def reset_cards(self, card1_index, card2_index):
        """Restablecer las cartas si no hay coincidencia"""
        self.buttons[card1_index].config(text="?", image="", relief="raised")
        self.buttons[card2_index].config(text="?", image="", relief="raised")

    def update_timer(self, time):
        """Actualizar el temporizador en la interfaz"""
        self.timer_label.config(text=f"Tiempo: {time}s")

    def update_moves(self, moves):
        """Actualizar el contador de movimientos"""
        self.moves_label.config(text=f"Movimientos: {moves}")
