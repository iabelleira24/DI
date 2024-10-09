class Heroe:
    def __init__(self, nombre):
        # Inicializa el héroe con su nombre, ataque, defensa, salud, y salud máxima.
        # También define si la defensa ha sido aumentada temporalmente.
        self.nombre = nombre
        self.ataque = 10  # Poder ofensivo del héroe
        self.defensa = 5  # Capacidad defensiva del héroe
        self.salud = 100  # Salud actual del héroe
        self.salud_maxima = 100  # Salud máxima del héroe
        self.defensa_aumentada = False  # Indica si la defensa ha sido aumentada temporalmente

    def atacar(self, enemigo):
        # El héroe ataca a un enemigo. El daño se calcula como la diferencia entre
        # el ataque del héroe y la defensa del enemigo.
        print(f"Héroe ataca a {enemigo.nombre}.")
        danio = self.ataque - enemigo.defensa  # Calcula el daño infligido
        if danio > 0:
            enemigo.salud -= danio  # Reduce la salud del enemigo si el daño es positivo
            print(f"El enemigo {enemigo.nombre} ha recibido {danio} puntos de daño.")
        else:
            # Si el daño es menor o igual a la defensa del enemigo, el ataque es bloqueado.
            print("El enemigo ha bloqueado el ataque.")

    def curarse(self):
        # El héroe se cura recuperando 20 puntos de salud, pero sin exceder su salud máxima.
        cura = 20
        self.salud = min(self.salud + cura, self.salud_maxima)  # La salud no puede exceder la salud máxima
        print(f"Héroe se ha curado. Salud actual: {self.salud}")

    def defenderse(self):
        # El héroe se defiende aumentando temporalmente su defensa en 5 puntos
        # hasta el siguiente turno.
        self.defensa += 5  # Aumenta temporalmente la defensa
        self.defensa_aumentada = True  # Marca que la defensa fue aumentada
        print(f"Héroe se defiende. Defensa aumentada temporalmente a {self.defensa}.")

    def reset_defensa(self):
        # Si la defensa ha sido aumentada temporalmente, este método restaura
        # la defensa a su valor original después del turno.
        if self.defensa_aumentada:
            self.defensa -= 5  # Restaura la defensa original
            self.defensa_aumentada = False  # Marca que la defensa ya no está aumentada
            print(f"La defensa de {self.nombre} vuelve a la normalidad.")

    def esta_vivo(self):
        # Verifica si el héroe sigue vivo, es decir, si su salud es mayor que 0.
        # Devuelve True si la salud es mayor a 0, de lo contrario devuelve False.
        return self.salud > 0

