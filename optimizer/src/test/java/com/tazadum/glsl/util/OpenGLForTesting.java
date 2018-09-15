package com.tazadum.glsl.util;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class OpenGLForTesting {
    private final GLWindow glWindow;

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final SynchronousQueue<OpenGLJob> workQueue = new SynchronousQueue<>();

    public OpenGLForTesting() {
        System.setProperty("java.awt.headless", "true");

        final Display dpy = NewtFactory.createDisplay(null);
        final Screen screen = NewtFactory.createScreen(dpy, 0);
        final GLCapabilitiesImmutable caps = new GLCapabilities(GLProfile.getDefault());

        glWindow = GLWindow.create(screen, caps);
        glWindow.setSize(100, 100);
        glWindow.setFullscreen(false);

        glWindow.addGLEventListener(new GLEventListener() {
            @Override
            public void init(GLAutoDrawable drawable) {
                initialized.set(true);
            }

            @Override
            public void dispose(GLAutoDrawable drawable) {
            }

            @Override
            public void display(GLAutoDrawable drawable) {
                performWork(drawable.getGL());
            }

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            }
        });
    }

    private void performWork(GL gl) {
        while (!workQueue.isEmpty()) {
            OpenGLJob job = workQueue.poll();

            try {
                job.execute(gl);
            } catch (Exception e) {
                job.onError(e);
            }
        }
    }


    public void destroy() {
        glWindow.destroy();
    }

    public void waitForInit() {
        long start = System.currentTimeMillis();
        while (!initialized.get()) {
            if (System.currentTimeMillis() - start > 5_000) {
                throw new IllegalStateException("Can't initialize OpenGL");
            }

            Thread.yield();
        }
    }

    public interface OpenGLJob {
        void execute(GL gl);

        void onError(Exception e);
    }
}
