import tkinter as tk

# Función para mostrar el saludo personalizado
def mostrar_saludo(entry, etiqueta):
    nombre = entry.get()  # Obtiene el nombre ingresado en el campo de texto
    saludo = f"¡Hola, {nombre}!"
    etiqueta.config(text=saludo)

# Función principal
def main():
    # Crear la ventana principal
    root = tk.Tk()
    root.title("Ejercicio 3: Entry")
    root.geometry("300x200")

    # Crear la etiqueta de instrucciones
    etiqueta_instrucciones = tk.Label(root, text="Escribe tu nombre:")
    etiqueta_instrucciones.pack(pady=10)

    # Crear el campo de entrada (Entry) para el nombre
    entry_nombre = tk.Entry(root)
    entry_nombre.pack(pady=10)

    # Crear la etiqueta donde se mostrará el saludo
    etiqueta_saludo = tk.Label(root, text="")
    etiqueta_saludo.pack(pady=10)

    # Crear el botón para mostrar el saludo personalizado
    boton_saludo = tk.Button(root, text="Mostrar saludo", command=lambda: mostrar_saludo(entry_nombre, etiqueta_saludo))
    boton_saludo.pack(pady=10)

    # Iniciar el bucle principal de la aplicación
    root.mainloop()

# Punto de entrada principal
if __name__ == "__main__":
    main()
