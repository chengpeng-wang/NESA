package org.mozilla.javascript;

import org.mozilla.javascript.ContextFactory.Listener;

@Deprecated
public interface ContextListener extends Listener {
    @Deprecated
    void contextEntered(Context context);

    @Deprecated
    void contextExited(Context context);
}
