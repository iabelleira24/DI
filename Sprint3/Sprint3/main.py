import tkinter as tk
from controlador import GameController

def main():
    # Crear la ventana principal (root) de Tkinter
    root = tk.Tk()
    root.title("Juego de Memoria")

    # Crear el controlador del juego
    controller = GameController(root)

    # Iniciar el bucle de eventos de Tkinter
    root.mainloop()

if __name__ == "__main__":
    main()