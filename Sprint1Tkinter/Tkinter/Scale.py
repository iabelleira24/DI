import tkinter as tk

# Función para actualizar la etiqueta con el valor de la barra deslizante
def actualizar_label():
    valor = scale.get()  # Obtiene el valor actual del Scale
    label.config(text=f"Valor seleccionado: {valor}")  # Actualiza la etiqueta

# Crear la ventana principal
root = tk.Tk()
root.title("Barra Deslizante (Scale) con Tkinter")
root.geometry("300x200")

# Variable de Tkinter para vincular a la barra deslizante
valor_variable = tk.IntVar()

# Crear la barra deslizante
scale = tk.Scale(
    root,
    from_=0,               # Rango mínimo
    to=100,                # Rango máximo
    orient=tk.HORIZONTAL,  # Orientación de la barra
    variable=valor_variable,  # Vincula la barra a la variable
    command=lambda _: actualizar_label()  # Llama a la función al mover la barra
)
scale.pack(pady=20)

# Crear una etiqueta para mostrar el valor seleccionado
label = tk.Label(root, text="Valor seleccionado: 0")
label.pack(pady=20)

# Iniciar el bucle principal de la aplicación
root.mainloop()
