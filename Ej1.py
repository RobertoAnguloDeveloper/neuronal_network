import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt

entrada = np.array([1,5,6,8,9,10,11,44,104,56,129,156,49,151,187,14,60,120,146,63,73,65,22,140,85,57,15,45,104,36,57,44,78,162,165,14,46,155,182,174,125,71,43,139,164,144,15,18,157,150,116,34,86,90,67,83,71], dtype=float)
salida = np.array([8,28,33,43,48,53,58,223,523,283,648,783,248,758,938,73,303,603,733,318,368,328,113,703,428,288,78,228,523,183,288,223,393,813,828,73,233,778,913,873,628,358,218,698,823,723,78,93,788,753,583,173,433,453,338,418,358], dtype=float)

capa1 = tf.keras.layers.Dense(units=1, input_shape=[1])

modelo = tf.keras.Sequential([capa1])

modelo.compile(
    optimizer = tf.keras.optimizers.Adam(0.1),
    loss = "mean_squared_error"
)

print("Entrenando la red")

entrenamiento = modelo.fit(entrada, salida, epochs=2900, verbose=False)

modelo.save('RedNeuronal.keras')
modelo.save_weights('Weights.keras')

plt.xlabel("Ciclos de entrenamiento")
plt.ylabel("Errores")

plt.plot(entrenamiento.history["loss"])

print("Terminamos")

i = input("Ingresa el numero: ")
i = float(i)

prediction = modelo.predict([i])

print("La prediccion es: " + str(prediction))
plt.show()