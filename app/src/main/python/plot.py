import matplotlib.pyplot as plt
import numpy as np
import io


def get_points(n) -> np.ndarray:
    np.random.seed(n+5)
    temp = 25
    # Crear una lista de puntos
    points = np.array([(i, temp:=temp+(0.5-np.random.rand(1)[0])) for i in range(1, np.random.randint(5, 10))])
    return points

# Dibujar los puntos hasta el índice actual
def draw_points(ax):
    colors = ['red', 'green', 'blue']
    for i in range(3):
        points = get_points(i)
        ax.scatter(points[:, 0], points[:, 1], color=colors[i])



def set_limits(plt):
    points = np.array([*get_points(0), *get_points(2), *get_points(3)])
    plt.xlim([0, len(points)/3 + 3])
    plt.ylim([np.min(points[:, 1])-0.2, np.max(points[:, 1])])


def clean(ax, fig):
    ax.set_ylabel('Temperatura')
    fig.set_size_inches(16, 9)
    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)


def show_interpolation(ax):
    colors = ['red', 'green', 'blue']
    for i in range(3):
        points = get_points(i)
        # Dibujar los segmentos entre los puntos hasta el índice actual
        for j in range(len(points)):
            ax.plot(points[j:j+2, 0], points[j:j+2, 1], color=colors[i], alpha=0.5)



def show_interpolation_graph():
    # Crear una figura y un eje
    fig, ax = plt.subplots()

    draw_points(ax=ax)

    show_interpolation(ax)

    set_limits(plt)

    clean(ax, fig)

    f = io.BytesIO()
    plt.savefig(f, format="png", bbox_inches='tight', dpi=75)

    return f.getvalue()