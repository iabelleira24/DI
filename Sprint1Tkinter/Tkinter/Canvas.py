import tkinter as tk

def dibujar_figuras():
    # Obtener las coordenadas ingresadas por el usuario
    try:
        x1 = int(entry_x1.get())
        y1 = int(entry_y1.get())
        x2 = int(entry_x2.get())
        y2 = int(entry_y2.get())

        # Limpiar el canvas antes de dibujar
        canvas.delete("all")

        # Dibujar el rectángulo
        canvas.create_rectangle(x1, y1, x2, y2, outline="blue", width=2)

        # Dibujar el círculo (que se inscribe en un rectángulo definido por las mismas coordenadas)
        radio = min(abs(x2 - x1), abs(y2 - y1)) // 2  # El círculo se ajusta al espacio disponible
        canvas.create_oval(x1 + radio, y1 + radio, x2 - radio, y2 - radio, outline="red", width=2)

    except ValueError:
        etiqueta_resultado.config(text="Por favor, ingresa coordenadas válidas")

# Crear la ventana principal
root = tk.Tk()
root.title("Dibujar Círculo y Rectángulo")
root.geometry("500x500")

# Crear un canvas
canvas = tk.Canvas(root, width=400, height=400, bg="white")
canvas.pack(pady=10)

# Crear una frame para organizar los inputs
frame = tk.Frame(root)
frame.pack()

# Crear etiquetas y campos de entrada para las coordenadas
label_x1 = tk.Label(frame, text="x1:")
label_x1.grid(row=0, column=0)
entry_x1 = tk.Entry(frame)
entry_x1.grid(row=0, column=1)

label_y1 = tk.Label(frame, text="y1:")
label_y1.grid(row=0, column=2)
entry_y1 = tk.Entry(frame)
entry_y1.grid(row=0, column=3)

label_x2 = tk.Label(frame, text="x2:")
label_x2.grid(row=1, column=0)
entry_x2 = tk.Entry(frame)
entry_x2.grid(row=1, column=1)

label_y2 = tk.Label(frame, text="y2:")
label_y2.grid(row=1, column=2)
entry_y2 = tk.Entry(frame)
entry_y2.grid(row=1, column=3)

# Crear el botón para dibujar las figuras
boton_dibujar = tk.Button(root, text="Dibujar", command=dibujar_figuras)
boton_dibujar.pack(pady=10)

# Crear una etiqueta para mostrar mensajes de error o éxito
etiqueta_resultado = tk.Label(root, text="")
etiqueta_resultado.pack(pady=5)

# Iniciar el bucle de la aplicación
root.mainloop()
