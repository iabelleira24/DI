import tkinter as tk

# Función para mostrar el mensaje en la etiqueta
def mostrar_mensaje(etiqueta):
    etiqueta.config(text="¡Has presionado el botón!")

# Función para cerrar la ventana
def cerrar_ventana(root):
    root.destroy()

# Función principal
def main():
    # Crear la ventana principal
    root = tk.Tk()
    root.title("Ejercicio 2: Botones")
    root.geometry("300x200")

    # Crear la etiqueta (inicialmente vacía)
    etiqueta = tk.Label(root, text="")
    etiqueta.pack(pady=20)

    # Crear el primer botón para mostrar un mensaje
    boton_mensaje = tk.Button(root, text="Mostrar mensaje", command=lambda: mostrar_mensaje(etiqueta))
    boton_mensaje.pack(pady=10)

    # Crear el segundo botón para cerrar la ventana
    boton_cerrar = tk.Button(root, text="Cerrar ventana", command=lambda: cerrar_ventana(root))
    boton_cerrar.pack(pady=10)

    # Iniciar el bucle principal de la aplicación
    root.mainloop()

# Punto de entrada principal
if __name__ == "__main__":
    main()
