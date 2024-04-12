import threading

class X:
    def __init__(self):
        self.lastIdUsed = 0

    def getNextId(self, t):
        with lock:
            print("Chamado por:", t, "- Last id used:", self.lastIdUsed)
            self.lastIdUsed += 1
            return self.lastIdUsed

def threadA(obj):
    for i in range(10):
        nextId = obj.getNextId("A")
        print("Thread A:", nextId)

def threadB(obj):
    for i in range(10):
        nextId = obj.getNextId("B")
        print("Thread B:", nextId)

if __name__ == "__main__":
    obj = X()
    lock = threading.Lock()

    # Criando e iniciando a primeira thread
    threadA = threading.Thread(target=threadA, args=(obj,))
    threadA.start()

    # Criando e iniciando a segunda thread
    threadB = threading.Thread(target=threadB, args=(obj,))
    threadB.start()

    # Aguardando o término das threads
    threadA.join()
    threadB.join()
