import tkinter as tk

# Función para cambiar el texto de la tercera etiqueta
def cambiar_texto(etiqueta):
    etiqueta.config(text="¡Texto cambiado!")

# Función principal que crea la ventana
def main():
    # Crear la ventana principal
    root = tk.Tk()  # Corregido: Tk() con 'k' minúscula
    root.title("Ejercicio de etiquetas")  # Corregido: 'title' en lugar de 'tittle'
    root.geometry("300x200")

    # Crear las etiquetas
    etiqueta1 = tk.Label(root, text="¡Bienvenido a la aplicación!")
    etiqueta1.pack(pady=10)

    etiqueta2 = tk.Label(root, text="Mi nombre es: [Iago]")
    etiqueta2.pack(pady=10)

    etiqueta3 = tk.Label(root, text="Haz click en el botón para cambiarme")
    etiqueta3.pack(pady=10)

    # Crear el botón que cambia el texto de la tercera etiqueta
    boton = tk.Button(root, text="Cambiar texto", command=lambda: cambiar_texto(etiqueta3))
    boton.pack(pady=10)

    # Iniciar el bucle principal de la aplicación
    root.mainloop()

# Punto de entrada principal
if __name__ == "__main__":
    main()
