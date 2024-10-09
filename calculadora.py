import os
import sys

# Remove '' and current working directory from the first entry
# of sys.path, if present to avoid using current directory
# in pip commands check, freeze, install, list and show,
# when invoked as python -m pip <command>
if sys.path[0] in ("", os.getcwd()):
    sys.path.pop(0)

# If we are running from a wheel, add the wheel to sys.path
# This allows the usage python pip-*.whl/pip install pip-*.whl
if __package__ == "":
    # __file__ is pip-*.whl/pip/__main__.py
    # first dirname call strips of '/__main__.py', second strips off '/pip'
    # Resulting path is the name of the wheel itself
    # Add that to sys.path so we can import pip
    path = os.path.dirname(os.path.dirname(__file__))
    sys.path.insert(0, path)

# calculadora.py
from operaciones import suma, resta, multiplicacion, division

def main():
    while True:
        # Solicitar los números al usuario
        try:
            num1 = float(input("Introduce el primer número: "))
            num2 = float(input("Introduce el segundo número: "))
        except ValueError:
            print("Error: Debes introducir números válidos.")
            continue

        # Solicitar la operación a realizar
        print("\n¿Qué operación deseas realizar?")
        print("1 - Suma")
        print("2 - Resta")
        print("3 - Multiplicación")
        print("4 - División")

        operacion = input("Selecciona una opción (1/2/3/4): ")

        # Realizar la operación seleccionada
        if operacion == "1":
            resultado = suma(num1, num2)
            print(f"\nEl resultado de la suma es: {resultado}\n")
        elif operacion == "2":
            resultado = resta(num1, num2)
            print(f"\nEl resultado de la resta es: {resultado}\n")
        elif operacion == "3":
            resultado = multiplicacion(num1, num2)
            print(f"\nEl resultado de la multiplicación es: {resultado}\n")
        elif operacion == "4":
            resultado = division(num1, num2)
            print(f"\nEl resultado de la división es: {resultado}\n")
        else:
            print("Opción no válida. Por favor selecciona 1, 2, 3 o 4.")
            continue

        # Preguntar si el usuario desea realizar otra operación
        otra_operacion = input("¿Quieres hacer otra operación? (s/n): ").lower()

        if otra_operacion != "s":
            print("Gracias por usar la calculadora. ¡Adiós!")
            break

if __name__ == "__main__":
    main()
