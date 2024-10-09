import random


class Tesoro:
    def __init__(self):
        # Inicializa los posibles beneficios que un tesoro puede otorgar.
        self.beneficios = ["aumento de ataque", "aumento de defensa", "restaurar salud"]

    def encontrar_tesoro(self, heroe):
        # Selecciona aleatoriamente un beneficio del tesoro y lo aplica al héroe.
        beneficio = random.choice(self.beneficios)  # Escoge un beneficio al azar
        print(f"Héroe ha encontrado un tesoro: {beneficio}.")

        if beneficio == "aumento de ataque":
            # Aumenta el ataque del héroe en 5 puntos si el beneficio es aumento de ataque.
            heroe.ataque += 5
            print(f"El ataque de {heroe.nombre} aumenta a {heroe.ataque}.")
        elif beneficio == "aumento de defensa":
            # Aumenta la defensa del héroe en 3 puntos si el beneficio es aumento de defensa.
            heroe.defensa += 3
            print(f"La defensa de {heroe.nombre} aumenta a {heroe.defensa}.")
        elif beneficio == "restaurar salud":
            # Restaura la salud del héroe a su salud máxima si el beneficio es restaurar salud.
            heroe.salud = heroe.salud_maxima
            print(f"La salud de {heroe.nombre} ha sido restaurada a {heroe.salud_maxima}.")
