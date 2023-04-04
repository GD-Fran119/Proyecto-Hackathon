import io
import matplotlib.pyplot as plt

def plot():
    xa = list(map(lambda x: str(x), [i for i in range(1, 11)]))
    ya = list(map(lambda x: str(x), [i for i in range(1, 11)]))

    fig, ax = plt.subplots()
    ax.plot(xa, ya)

    f = io.BytesIO()
    plt.savefig(f, format="png")
    return f.getvalue()