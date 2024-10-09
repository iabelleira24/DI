# operaciones.py

def suma(a, b):
    """
    Esta función devuelve la suma de dos números.
    """
    return a + b

def resta(a, b):
    """
    Esta función devuelve la resta de dos números.
    """
    return a - b

def multiplicacion(a, b):
    """
    Esta función devuelve la multiplicación de dos números.
    """
    return a * b

def division(a, b):
    """
    Esta función devuelve la división de dos números.
    Si el divisor es 0, retorna un mensaje de error.
    """
    if b == 0:
        return "Error: No se puede dividir entre 0"
    return a / b
