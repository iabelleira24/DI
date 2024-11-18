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
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/125-1251347_jigglypuff-drawing-pencil-single-pokemon-images-with-name.png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/967985ca22b2b00b485eca2bd47295d2.png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(1).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(2).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(3).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(4).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(5).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(6).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP.jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(1).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(1).png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(2).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(2).png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(3).png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(4).png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R.jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R.png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/carta%20comun.png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/png-transparent-pokemon-go-pokemon-art-academy-pikachu-drawing-pokemon-cartoon-fictional-character-pokemon.png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/png-transparent-pokemon-go-pokemon-x-and-y-ash-ketchum-charmander-pokemon-background-orange-cartoon-fictional-character.png",

            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(5).png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/R%20(6).png",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(7).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(8).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(9).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(10).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(11).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(12).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(13).jpg",
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/OIP%20(14).jpg"
            "https://raw.githubusercontent.com/iabelleira24/DI/main/Sprint3/imagenes%20poke/ce4f00bd6e65ce915aaa61bc5c0eb315.jpg"
        ]
        self.images = []

        self.main_menu = MainMenu(self.root, self.start_game_callback, self.show_stats_callback, self.quit_callback)

    def start_game_callback(self):
        """Iniciar el juego y seleccionar la dificultad."""
        # Limpiar el estado del juego anterior
        self.game_model = None
        self.game_view = None
        self.selected = []
        self.move_count = 0
        self.start_time = 0
        self.images_loaded = False
        self.images = []

        # Mostrar la selección de dificultad
        self.show_difficulty_selection()

    def show_difficulty_selection(self):
        """Seleccionar la dificultad y generar el tablero de juego."""
        difficulty = simpledialog.askstring("Selecciona la dificultad",
                                            "Elige la dificultad: fácil, medio, difícil",
                                            parent=self.root).lower()

        if difficulty is None:
            return  # Regresa al menú principal, no hace nada

        # Pedir el nombre del jugador, y si se cancela, volver al menú principal
        self.player_name = self.main_menu.ask_player_name()
        if self.player_name is None:
            return  # Si se cancela, regresamos al menú principal

        if difficulty in ["facil", "medio", "dificil"]:
            self.difficulty = difficulty
            self.game_model = GameModel(self.difficulty)  # Crear el modelo de juego
            self.game_model.load_scores()  # Cargar las puntuaciones desde el archivo
            self.game_model._generate_board()  # Generar el tablero

            # Mostrar ventana de carga mientras se descargan las imágenes
            self.show_loading_window()

            # Iniciar la descarga de imágenes en un hilo
            self.load_images_thread()
        else:
            messagebox.showwarning("Dificultad inválida", "Por favor elige una dificultad válida.")

    def show_loading_window(self):
        """Mostrar ventana de carga mientras las imágenes se descargan."""
        if hasattr(self, 'loading_window') and self.loading_window.winfo_exists():
            self.loading_window.destroy()  # Si la ventana ya existe, la destruimos

        self.loading_window = tk.Toplevel(self.root)  # Crear ventana para la carga
        self.loading_window.title("Cargando imágenes")
        self.loading_label = tk.Label(self.loading_window, text="Cargando imágenes, por favor espere...",
                                      font=("Arial", 14))
        self.loading_label.pack(padx=20, pady=20)
        self.loading_window.protocol("WM_DELETE_WINDOW", self.prevent_window_close)  # Evitar cerrar la ventana

    def prevent_window_close(self):
        """Prevenir que la ventana de carga se cierre mientras se están descargando las imágenes."""
        pass

    def load_images_thread(self):
        """Iniciar un hilo para descargar las imágenes sin bloquear la interfaz de usuario."""
        threading.Thread(target=self.load_images).start()

    def load_images(self):
        """Descargar las imágenes desde las URLs y almacenarlas en la lista."""
        try:
            for url in self.image_urls:
                # Iniciar un hilo para descargar la imagen
                threading.Thread(target=self.iniciar_imagen, args=(url,)).start()
        except Exception as e:
            print(f"Error al intentar cargar imágenes: {e}")

    def iniciar_imagen(self, url):
        """Función que descarga la imagen en un hilo separado."""
        # Pasar el callback de la función descargar_imagen
        descargar_imagen(url, self.image_downloaded_callback)

    def image_downloaded_callback(self, imagen):
        """Callback que maneja la imagen descargada."""
        if imagen:
            self.images.append(imagen)  # Añadir la imagen descargada a la lista
        else:
            print("Error al descargar una imagen.")

        self.images_loaded = True  # Indicamos que las imágenes están cargadas
        self.check_images_loaded()  # Comprobar si todas las imágenes han sido descargadas

    def check_images_loaded(self):
        """Verificar si todas las imágenes están cargadas y proceder con la vista de juego."""
        if len(self.images) == len(self.image_urls):
            # Aquí puedes proceder con la lógica para continuar con el juego
            print("Todas las imágenes están cargadas")
            # Cerrar la ventana de carga
            self.loading_window.destroy()  # Cerrar la ventana de carga

            # Proceder a la creación de la vista del tablero
            if self.game_view:
                self.game_view.frame.destroy()  # Destruir la vista anterior si existe

            self.start_game_window()  # Iniciar la nueva ventana de juego

    def start_game_window(self):
        """Abrir la ventana para mostrar el tablero de juego"""
        self.root.withdraw()  # Ocultar la ventana principal del menú
        game_window = tk.Toplevel(self.root)  # Crear nueva ventana para el juego
        game_window.title("Juego de Memoria")

        # Crear la vista del tablero de juego
        self.game_view = GameView(game_window, self.game_model, self)
        self.game_view.create_board()  # Crear el tablero de juego

        # Iniciar el temporizador y actualizaciones
        self.update_time()  # Llamada recursiva para el temporizador

    def on_card_click(self, index):
        """Acción cuando se hace clic en una carta"""
        # Verificar si ya hay dos cartas volteadas o si el índice ya está en selected
        if len(self.selected) == 2 or any(index == selected[0] for selected in self.selected):
            return  # Si ya hay dos cartas volteadas o es la misma carta, no hacer nada

        card_value = self.game_model.board[index]
        self.selected.append((index, card_value))  # Añadir la carta a la lista de cartas volteadas

        # Mostrar temporalmente la carta en la vista
        self.game_view.update_board([(index, card_value)])

        # Verificar si se voltearon dos cartas
        if len(self.selected) == 2:
            self.handle_card_selection()

    def handle_card_selection(self):
        """Verificar si las cartas seleccionadas coinciden"""
        card1_index, card1_value = self.selected[0]
        card2_index, card2_value = self.selected[1]

        if card1_value == card2_value:
            # Las cartas coinciden, actualizarlas en la vista
            self.game_view.update_board(self.selected, matched=True)
            # Comprobar si se ha ganado el juego
            self.check_victory()
        else:
            # Las cartas no coinciden, ocultarlas después de un retraso
            self.root.after(1000, self.game_view.reset_cards, card1_index, card2_index)

        # Incrementar el contador de movimientos y actualizar la vista
        self.increment_move_count()
        # Restablecer las cartas seleccionadas
        self.selected = []

    def increment_move_count(self):
        """Incrementar el contador de movimientos y actualizar la vista"""
        self.move_count += 1
        self.game_view.update_moves(self.move_count)

    def update_time(self):
        """Actualizar el temporizador cada segundo"""
        if self.root.winfo_exists():  # Verificamos si la ventana principal sigue existiendo
            self.start_time += 1
            self.game_view.update_timer(self.start_time)
            self.root.after(1000, self.update_time)  # Llamar a la función cada segundo

    def check_victory(self):
        """Comprobar si todas las cartas han sido emparejadas"""
        if len(self.game_view.matched_cards) == len(self.game_model.board):
            messagebox.showinfo("¡Felicidades!", "¡Has ganado!")

            # Guardar la puntuación en el modelo
            self.game_model.save_score(self.player_name, self.move_count, self.start_time)

            # Después de un breve retraso, regresar al menú principal
            self.root.after(100, self.return_to_main_menu)

    def return_to_main_menu(self):
        """Redirigir al menú principal sin cerrar la aplicación"""
        self.game_view.frame.destroy()  # Destruir el marco del juego
        self.game_view = None  # Limpiar la referencia de la vista
        self.selected = []  # Limpiar las cartas seleccionadas
        self.move_count = 0  # Reiniciar el contador de movimientos
        self.start_time = 0  # Reiniciar el tiempo
        self.images_loaded = False  # Restablecer el indicador de imágenes cargadas
        self.images = []  # Limpiar la lista de imágenes descargadas

        self.root.deiconify()  # Mostrar el menú principal nuevamente

    def show_stats_callback(self):
        """Mostrar las estadísticas del juego"""
        # Aquí comprobamos si self.game_model es None, pero aún así procedemos a mostrar las puntuaciones
        if self.game_model is None:
            messagebox.showinfo("Estadísticas", "No hay estadísticas disponibles. No se ha jugado ningún juego aún.")
            return

        # Asegúrate de que las puntuaciones estén cargadas (aunque el juego no haya comenzado)
        self.game_model.load_scores()

        scores = self.game_model.scores  # Acceder a las puntuaciones guardadas en el modelo
        stats_message = "Ranking por dificultad:\n"

        for difficulty, score_list in scores.items():
            stats_message += f"\n{difficulty.capitalize()}:\n"
            for idx, entry in enumerate(score_list):
                stats_message += f"{idx + 1}. {entry['name']} - Movimientos: {entry['moves']}, Tiempo: {entry['time_taken']}s, Fecha: {entry['date']}\n"

        messagebox.showinfo("Estadísticas", stats_message)

    def quit_callback(self):
        """Cerrar la aplicación"""
        self.root.quit()