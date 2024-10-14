class Monstruo:
    def __init__(self, nombre, ataque, defensa, salud):
        # Inicializa al monstruo con su nombre, ataque, defensa y salud.
        self.nombre = nombre  # Nombre del monstruo
        self.ataque = ataque  # Poder ofensivo del monstruo
        self.defensa = defensa  # Capacidad defensiva del monstruo
        self.salud = salud  # Salud actual del monstruo

    def atacar(self, heroe):
        # El monstruo ataca al héroe. El daño se calcula como la diferencia entre
        # el ataque del monstruo y la defensa del héroe.
        print(f"El monstruo {self.nombre} ataca a {heroe.nombre}.")
        danio = self.ataque - heroe.defensa  # Calcula el daño infligido
        if danio > 0:
            heroe.salud -= danio  # Reduce la salud del héroe si el daño es positivo
            print(f"El héroe {heroe.nombre} ha recibido {danio} puntos de daño.")
        else:
            # Si el daño es menor o igual a la defensa del héroe, el ataque es bloqueado.
            print("El héroe ha bloqueado el ataque.")

    def esta_vivo(self):
        # Verifica si el monstruo sigue vivo, es decir, si su salud es mayor que 0.
        # Devuelve True si la salud es mayor a 0, de lo contrario devuelve False.
        return self.salud > 0
