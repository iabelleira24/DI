import tkinter as tk
from tkinter import simpledialog
from tkinter import messagebox
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
        self.title_label = tk.Label(self.frame, text="Juego de Memoria", font=("Arial", 24))
        self.title_label.pack(pady=20)

        # Botón para iniciar el juego
        self.start_button = tk.Button(self.frame, text="Jugar", command=self.start_game_callback)
        self.start_button.pack(pady=10)

        # Botón para mostrar las estadísticas
        self.stats_button = tk.Button(self.frame, text="Estadísticas", command=self.show_stats_callback)
        self.stats_button.pack(pady=10)

        # Botón para salir
        self.quit_button = tk.Button(self.frame, text="Salir", command=self.quit_callback)
        self.quit_button.pack(pady=10)

    def ask_player_name(self):
        # Pedir el nombre del jugador
        name = simpledialog.askstring("Nombre del jugador", "¿Cuál es tu nombre?", parent=self.root)

        if name is None:  # Si el usuario presiona "Cancelar" o cierra la ventana
            return None  # Regresar None para indicar que no se ingresó un nombre

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

        # Etiqueta para el temporizador y el contador de movimientos
        self.timer_label = tk.Label(self.frame, text="Tiempo: 0s", font=("Arial", 14))
        self.timer_label.grid(row=10, column=0, columnspan=2, pady=10)

        self.moves_label = tk.Label(self.frame, text="Movimientos: 0", font=("Arial", 14))
        self.moves_label.grid(row=10, column=2, columnspan=2, pady=10)

    def create_board(self):
        """Crear el tablero de juego con las cartas"""
        num_cards = len(self.game_model.board)
        rows = self.game_model.rows
        cols = self.game_model.cols
        index = 0

        # Crear los botones y configurarlos en el grid
        for row in range(rows):
            for col in range(cols):
                if index < num_cards:
                    # Aseguramos que los botones sean suficientemente grandes para las imágenes
                    card_button = tk.Button(self.frame, text="?", width=10, height=5,  # Aumentar tamaño del botón
                                            command=lambda index=index: self.game_controller.on_card_click(index))
                    card_button.grid(row=row, column=col, padx=5, pady=5, sticky="nsew")  # Usamos sticky para expandir
                    self.buttons.append(card_button)
                    index += 1

        # Ajustar las filas y columnas para expandirse correctamente
        for i in range(rows):
            self.frame.grid_rowconfigure(i, weight=1, uniform="equal")  # Igualar el tamaño de las filas
        for j in range(cols):
            self.frame.grid_columnconfigure(j, weight=1, uniform="equal")  # Igualar el tamaño de las columnas

    def update_board(self, selected, matched=False):
        """Actualizar la vista del tablero para mostrar cartas seleccionadas o emparejadas"""
        for index, card_value in selected:
            if matched:
                self.buttons[index].config(state="disabled", relief="sunken")
                self.matched_cards.append(index)
            else:
                # Verificar si hay imágenes descargadas
                if self.game_controller.images and card_value < len(self.game_controller.images):
                    img = self.game_controller.images[card_value]  # Obtener la imagen para la carta
                    img = img.resize((100, 100))  # Redimensionamos la imagen para que ocupe todo el tamaño del botón
                    img_tk = ImageTk.PhotoImage(img)

                    # Configuramos la imagen de la carta
                    self.buttons[index].config(image=img_tk, text="", relief="sunken")
                    self.buttons[index].image = img_tk  # Necesario para evitar que la imagen se elimine
                else:
                    # Si no hay imagen o el índice es incorrecto, mostrar el valor por defecto
                    self.buttons[index].config(text=f"Card {card_value + 1}")

    def reset_cards(self, card1_index, card2_index):
        """Restablecer las cartas si no coinciden"""
        # Restaurar las cartas a su estado original (text="?", sin imagen, y relief="raised")
        self.buttons[card1_index].config(text="?", image="", relief="raised")
        self.buttons[card2_index].config(text="?", image="", relief="raised")

    def update_timer(self, time):
        """Actualizar la etiqueta del temporizador"""
        self.timer_label.config(text=f"Tiempo: {time}s")

    def update_moves(self, moves):
        """Actualizar la etiqueta de movimientos"""
        self.moves_label.config(text=f"Movimientos: {moves}")