import tkinter as tk
from controlador import GameController
from modelo import GameModel

if __name__ == "__main__":
    root = tk.Tk()
    root.title("Juego de Memoria")

    # Inicializa el modelo con dificultad y nombre de jugador por defecto
    model = GameModel(difficulty='medium', player_name='Jugador')

    # Controlador como intermediario entre el modelo y la interfaz gráfica
    controller = GameController(root, model)

    # Ejecuta el bucle principal de Tkinter
    root.mainloop()
