import random
import tkinter as tk
from tkinter import messagebox

class JuegoPiedraPapelTijera:
    def __init__(self, master):
        self.master = master
        self.master.title("Piedra, Papel o Tijera")

        # Variables de juego para llevar la cuenta de los puntos
        self.puntos_jugador1 = 0
        self.puntos_jugador2 = 0

        # Crear menú principal
        self.menu = tk.Menu(master)
        master.config(menu=self.menu)

        # Submenú de opciones de juego
        self.juego_menu = tk.Menu(self.menu)
        self.menu.add_cascade(label="Juego", menu=self.juego_menu)
        self.juego_menu.add_command(label="Un jugador", command=self.jugar_un_jugador)
        self.juego_menu.add_command(label="Dos jugadores", command=self.jugar_dos_jugadores)
        self.juego_menu.add_separator()
        self.juego_menu.add_command(label="Salir", command=self.salir_del_juego)

        # Área de texto para mostrar los resultados de cada ronda
        self.resultados_text = tk.Text(master, height=10, width=50)
        self.resultados_text.pack()

        # Etiqueta para mostrar los puntos de los jugadores
        self.label_puntos = tk.Label(master,
                                     text=f"Puntos - Jugador 1: {self.puntos_jugador1} | Jugador 2: {self.puntos_jugador2}")
        self.label_puntos.pack()

    # Función para el modo de un jugador
    def jugar_un_jugador(self):
        self.resetear_puntos()
        self.mostrar_botones("Un jugador")

    # Función para el modo de dos jugadores
    def jugar_dos_jugadores(self):
        self.resetear_puntos()
        self.mostrar_botones("Dos jugadores")

    # Función para reiniciar los puntos y limpiar el área de resultados
    def resetear_puntos(self):
        self.puntos_jugador1 = 0
        self.puntos_jugador2 = 0
        self.resultados_text.delete(1.0, tk.END)
        self.actualizar_puntos()

    # Función para mostrar botones de jugada
    def mostrar_botones(self, modo):
        self.opciones = ["Piedra", "Papel", "Tijera"]
        self.modo = modo

        # Crear un frame para los botones de elección
        self.frame_botones = tk.Frame(self.master)
        self.frame_botones.pack()

        if modo == "Un jugador":
            self.crear_botones_juego("Jugador 1")
        else:
            self.crear_botones_juego("Jugador 1")
            self.crear_botones_juego("Jugador 2")

    # Función para crear botones de elección
    def crear_botones_juego(self, jugador):
        opciones = ["Piedra", "Papel", "Tijera"]
        label = tk.Label(self.frame_botones, text=f"Elige tu jugada {jugador}:")
        label.pack()

        for opcion in opciones:
            if jugador == "Jugador 1":
                tk.Button(self.frame_botones, text=opcion, command=lambda o=opcion: self.jugar_ronda(o, "Jugador 1")).pack(side=tk.LEFT)
            else:
                tk.Button(self.frame_botones, text=opcion, command=lambda o=opcion: self.jugar_ronda(o, "Jugador 2")).pack(side=tk.LEFT)

    # Función para gestionar cada ronda de juego
    def jugar_ronda(self, eleccion, jugador):
        if self.modo == "Un jugador":
            eleccion_jugador1 = eleccion
            eleccion_jugador2 = random.choice(self.opciones)
            self.resultados_text.insert(tk.END, f"El Jugador 1 saca {eleccion_jugador1}, la máquina saca {eleccion_jugador2}.\n")
            resultado = self.determinar_ganador(eleccion_jugador1, eleccion_jugador2, "Jugador 1", "máquina")
            self.resultados_text.insert(tk.END, resultado + "\n")
            self.actualizar_puntos()

        else:
            if jugador == "Jugador 2":
                eleccion_jugador1 = self.eleccion_jugador1
                eleccion_jugador2 = eleccion
                resultado = self.determinar_ganador(eleccion_jugador1, eleccion_jugador2, "Jugador 1", "Jugador 2")
                self.resultados_text.insert(tk.END, resultado + "\n")
                self.actualizar_puntos()

            else:
                self.eleccion_jugador1 = eleccion

        # Comprobar si alguien ha ganado
        if self.puntos_jugador1 == 3 or self.puntos_jugador2 == 3:
            ganador = "Jugador 1" if self.puntos_jugador1 == 3 else "Jugador 2" if self.modo == "Dos jugadores" else "Máquina"
            self.mostrar_ganador(ganador)
            self.frame_botones.destroy()
            self.resetear_puntos()  # Reiniciar los puntos después de que alguien gane

    # Función para determinar el ganador de una ronda
    def determinar_ganador(self, eleccion_jugador1, eleccion_jugador2, nombre_jugador1, nombre_jugador2):
        if eleccion_jugador1 == eleccion_jugador2:
            return "Es un empate"
        elif (eleccion_jugador1 == "Piedra" and eleccion_jugador2 == "Tijera") or \
                (eleccion_jugador1 == "Papel" and eleccion_jugador2 == "Piedra") or \
                (eleccion_jugador1 == "Tijera" and eleccion_jugador2 == "Papel"):
            self.puntos_jugador1 += 1
            return f"Gana el {nombre_jugador1}, {eleccion_jugador1} gana a {eleccion_jugador2}."
        else:
            self.puntos_jugador2 += 1
            return f"Gana el {nombre_jugador2}, {eleccion_jugador2} gana a {eleccion_jugador1}."

    # Función para actualizar los puntos en la interfaz
    def actualizar_puntos(self):
        self.label_puntos.config(text=f"Puntos - Jugador 1: {self.puntos_jugador1} | Jugador 2: {self.puntos_jugador2}")

    # Función para mostrar el ganador del juego
    def mostrar_ganador(self, ganador):
        messagebox.showinfo("Fin del juego", f"Gana el juego: {ganador}!")

    # Función para salir correctamente del juego
    def salir_del_juego(self):
        self.master.destroy()


# Ejecutar el juego
if __name__ == "__main__":
    root = tk.Tk()
    app = JuegoPiedraPapelTijera(root)
    root.mainloop()
