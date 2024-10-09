import random
from Tesoro import Tesoro
from Monstruo import Monstruo

class Mazmorra:
    def __init__(self, heroe):
        # Inicializa la mazmorra con el héroe que explora, una lista de monstruos y el tesoro.
        self.heroe = heroe  # El héroe que controlará el jugador
        # Crea una lista de monstruos que el héroe encontrará en la mazmorra.
        self.monstruos = [
            Monstruo("Goblin", 8, 3, 30),  # Monstruo Goblin con ataque 8, defensa 3, y salud 30
            Monstruo("Orco", 12, 5, 40),   # Monstruo Orco con ataque 12, defensa 5, y salud 40
            Monstruo("Dragón", 15, 8, 60)  # Monstruo Dragón con ataque 15, defensa 8, y salud 60
        ]
        self.tesoro = Tesoro()  # Instancia de la clase Tesoro

    def jugar(self):
        # Controla el flujo principal del juego. El héroe explora la mazmorra y lucha contra los monstruos.
        print("Héroe entra en la mazmorra.")
        # El héroe se enfrenta a cada monstruo en la lista.
        for monstruo in self.monstruos:
            print(f"Te has encontrado con un {monstruo.nombre}.")
            # Llama al método enfrentar_enemigo para iniciar el combate con el monstruo.
            self.enfrentar_enemigo(monstruo)
            # Si el héroe es derrotado, el juego termina.
            if not self.heroe.esta_vivo():
                print("Héroe ha sido derrotado en la mazmorra.")
                return
            # Si el héroe derrota al monstruo, busca un tesoro.
            self.buscar_tesoro()
        # Si el héroe derrota a todos los monstruos, conquista la mazmorra.
        print(f"¡{self.heroe.nombre} ha derrotado a todos los monstruos y ha conquistado la mazmorra!")

    def enfrentar_enemigo(self, enemigo):
        # Gestiona el combate entre el héroe y un monstruo.
        while enemigo.esta_vivo() and self.heroe.esta_vivo():
            print("\n¿Qué deseas hacer?")
            print("1. Atacar")
            print("2. Defender")
            print("3. Curarse")
            opcion = input("> ")  # El jugador elige una acción

            if opcion == "1":
                # El héroe ataca al monstruo.
                self.heroe.atacar(enemigo)
            elif opcion == "2":
                # El héroe se defiende, aumentando temporalmente su defensa.
                self.heroe.defenderse()
            elif opcion == "3":
                # El héroe se cura, restaurando parte de su salud.
                self.heroe.curarse()
            else:
                # Si el jugador elige una opción no válida, se muestra un mensaje de error.
                print("Opción no válida.")

            # Después de la acción del héroe, el monstruo contraataca si sigue vivo.
            if enemigo.esta_vivo():
                enemigo.atacar(self.heroe)
                # Restablece la defensa del héroe a su valor original si este se había defendido.
                self.heroe.reset_defensa()

    def buscar_tesoro(self):
        # Busca un tesoro después de derrotar a un monstruo.
        print("Buscando tesoro...")
        # Llama al método encontrar_tesoro del tesoro, aplicando un beneficio aleatorio al héroe.
        self.tesoro.encontrar_tesoro(self.heroe)
