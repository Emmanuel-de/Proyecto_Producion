Requisitos

Java 17 o superior

Cualquier IDE (IntelliJ / Eclipse / VS Code) o javac/java por consola

Ejecución rápida en IDE

Crea un proyecto Java y respeta el paquete com.factorysim y subpaquetes model y ui.

Copia cada archivo en su ruta.

Ejecuta App.java.

Compilación por consola

Dentro de la carpeta src/main/java (ajusta rutas según tu estructura):

javac -d out $(find . -name "*.java")
java -cp out com.factorysim.App

Empaquetar .jar ejecutable

Desde la raíz del proyecto donde out/ contiene las clases compiladas:

jar --create --file factory-sim.jar --main-class com.factorysim.App -C out .

Y ejecútalo con:

java -jar factory-sim.jar

Controles

Iniciar: lanza hilos de máquinas y vehículos

Pausar: congela la producción/entrega sin terminar los hilos

Reanudar: continúa

Reiniciar: detiene simulación, limpia almacén y estadísticas

Notas técnicas

Almacén: LinkedBlockingQueue con capacidad configurable

Sincronización: flags running/paused con AtomicBoolean, contadores con AtomicInteger

Seguridad de hilos: listas de solo lectura para UI, lista concurrente para tránsito

Actualización UI: javax.swing.Timer cada 300 ms

Siguientes mejoras (opcionales)

Destinos múltiples con tiempos/colas por destino

Prioridades por tipo de producto

Fallas/MTBF de máquinas y mantenimiento

Métricas de rendimiento (lead time, WIP, throughput) y gráficos

Persistencia de logs/estadísticas a CSV