package com.esotericsoftware.kryonet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listener {

    public static abstract class QueuedListener extends Listener {
        final Listener listener;

        public abstract void queue(Runnable runnable);

        public QueuedListener(Listener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("listener cannot be null.");
            }
            this.listener = listener;
        }

        public void connected(final Connection connection) {
            queue(new Runnable() {
                public void run() {
                    QueuedListener.this.listener.connected(connection);
                }
            });
        }

        public void disconnected(final Connection connection) {
            queue(new Runnable() {
                public void run() {
                    QueuedListener.this.listener.disconnected(connection);
                }
            });
        }

        public void received(final Connection connection, final Object obj) {
            queue(new Runnable() {
                public void run() {
                    QueuedListener.this.listener.received(connection, obj);
                }
            });
        }

        public void idle(final Connection connection) {
            queue(new Runnable() {
                public void run() {
                    QueuedListener.this.listener.idle(connection);
                }
            });
        }
    }

    public static class ReflectionListener extends Listener {
        private final HashMap<Class, Method> classToMethod = new HashMap();

        public void received(Connection connection, Object obj) {
            Class cls = obj.getClass();
            Method method = (Method) this.classToMethod.get(cls);
            if (method == null) {
                if (!this.classToMethod.containsKey(cls)) {
                    try {
                        method = getClass().getMethod("received", new Class[]{Connection.class, cls});
                    } catch (SecurityException e) {
                        return;
                    } catch (NoSuchMethodException e2) {
                        return;
                    } finally {
                        this.classToMethod.put(cls, method);
                    }
                } else {
                    return;
                }
            }
            try {
                method.invoke(this, new Object[]{connection, obj});
            } catch (Throwable th) {
                Throwable th2 = th;
                if ((th2 instanceof InvocationTargetException) && th2.getCause() != null) {
                    th2 = th2.getCause();
                }
                if (th2 instanceof RuntimeException) {
                    RuntimeException runtimeException = (RuntimeException) th2;
                }
                RuntimeException runtimeException2 = new RuntimeException("Error invoking method: " + getClass().getName() + "#received(Connection, " + cls.getName() + ")", th2);
            }
        }
    }

    public static class LagListener extends QueuedListener {
        private final int lagMillisMax;
        private final int lagMillisMin;
        final LinkedList<Runnable> runnables = new LinkedList();
        private final ScheduledExecutorService threadPool;

        public LagListener(int i, int i2, Listener listener) {
            super(listener);
            this.lagMillisMin = i;
            this.lagMillisMax = i2;
            this.threadPool = Executors.newScheduledThreadPool(1);
        }

        public void queue(Runnable runnable) {
            synchronized (this.runnables) {
                this.runnables.addFirst(runnable);
            }
            this.threadPool.schedule(new Runnable() {
                public void run() {
                    Runnable runnable;
                    synchronized (LagListener.this.runnables) {
                        runnable = (Runnable) LagListener.this.runnables.removeLast();
                    }
                    runnable.run();
                }
            }, (long) (this.lagMillisMin + ((int) (Math.random() * ((double) (this.lagMillisMax - this.lagMillisMin))))), TimeUnit.MILLISECONDS);
        }
    }

    public static class ThreadedListener extends QueuedListener {
        protected final ExecutorService threadPool;

        public ThreadedListener(Listener listener) {
            this(listener, Executors.newFixedThreadPool(1));
        }

        public ThreadedListener(Listener listener, ExecutorService executorService) {
            super(listener);
            if (executorService == null) {
                throw new IllegalArgumentException("threadPool cannot be null.");
            }
            this.threadPool = executorService;
        }

        public void queue(Runnable runnable) {
            this.threadPool.execute(runnable);
        }
    }

    public void connected(Connection connection) {
    }

    public void disconnected(Connection connection) {
    }

    public void received(Connection connection, Object obj) {
    }

    public void idle(Connection connection) {
    }
}
