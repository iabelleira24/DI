import tkinter as tk
from controlador import GameController

def center_window(window, width=800, height=600):
    """Centralizar la ventana en la pantalla."""
    screen_width = window.winfo_screenwidth()
    screen_height = window.winfo_screenheight()
    x = (screen_width - width) // 2
    y = (screen_height - height) // 2
    window.geometry(f"{width}x{height}+{x}+{y}")

def on_close(root):
    """Solicitar confirmación al cerrar la aplicación."""
    if tk.messagebox.askokcancel("Salir", "¿Estás seguro de que deseas salir?"):
        root.destroy()

def main():
    # Crear la ventana principal (root) de Tkinter
    root = tk.Tk()
    root.title("Juego de Memoria")

    # Configurar tamaño inicial y centrar ventana
    center_window(root, width=800, height=600)

    # Manejar el cierre de la ventana
    root.protocol("WM_DELETE_WINDOW", lambda: on_close(root))

    # Crear el controlador del juego
    controller = GameController(root)

    # Iniciar el bucle de eventos de Tkinter
    root.mainloop()

if __name__ == "__main__":
    main()
