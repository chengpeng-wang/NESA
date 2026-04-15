package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

class VastResource implements Serializable {
    private static final List<String> VALID_APPLICATION_TYPES = Arrays.asList(new String[]{"application/x-javascript"});
    private static final List<String> VALID_IMAGE_TYPES = Arrays.asList(new String[]{"image/jpeg", "image/png", "image/bmp", "image/gif"});
    private static final long serialVersionUID = 0;
    @NonNull
    private CreativeType mCreativeType;
    private int mHeight;
    @NonNull
    private String mResource;
    @NonNull
    private Type mType;
    private int mWidth;

    enum CreativeType {
        NONE,
        IMAGE,
        JAVASCRIPT
    }

    enum Type {
        STATIC_RESOURCE,
        HTML_RESOURCE,
        IFRAME_RESOURCE
    }

    @Nullable
    static VastResource fromVastResourceXmlManager(@NonNull VastResourceXmlManager resourceXmlManager, int width, int height) {
        for (Type fromVastResourceXmlManager : Type.values()) {
            VastResource fromVastResourceXmlManager2 = fromVastResourceXmlManager(resourceXmlManager, fromVastResourceXmlManager, width, height);
            if (fromVastResourceXmlManager2 != null) {
                return fromVastResourceXmlManager2;
            }
        }
        return null;
    }

    @Nullable
    static VastResource fromVastResourceXmlManager(@NonNull VastResourceXmlManager resourceXmlManager, @NonNull Type type, int width, int height) {
        CreativeType creativeType;
        Preconditions.checkNotNull(resourceXmlManager);
        Preconditions.checkNotNull(type);
        String iFrameResource = resourceXmlManager.getIFrameResource();
        String hTMLResource = resourceXmlManager.getHTMLResource();
        String staticResource = resourceXmlManager.getStaticResource();
        String staticResourceType = resourceXmlManager.getStaticResourceType();
        if (type != Type.STATIC_RESOURCE || staticResource == null || staticResourceType == null || !(VALID_IMAGE_TYPES.contains(staticResourceType) || VALID_APPLICATION_TYPES.contains(staticResourceType))) {
            if (type == Type.HTML_RESOURCE && hTMLResource != null) {
                creativeType = CreativeType.NONE;
                staticResource = hTMLResource;
            } else if (type != Type.IFRAME_RESOURCE || iFrameResource == null) {
                return null;
            } else {
                creativeType = CreativeType.NONE;
                staticResource = iFrameResource;
            }
        } else if (VALID_IMAGE_TYPES.contains(staticResourceType)) {
            creativeType = CreativeType.IMAGE;
        } else {
            creativeType = CreativeType.JAVASCRIPT;
        }
        return new VastResource(staticResource, type, creativeType, width, height);
    }

    VastResource(@NonNull String resource, @NonNull Type type, @NonNull CreativeType creativeType, int width, int height) {
        Preconditions.checkNotNull(resource);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(creativeType);
        this.mResource = resource;
        this.mType = type;
        this.mCreativeType = creativeType;
        this.mWidth = width;
        this.mHeight = height;
    }

    @NonNull
    public String getResource() {
        return this.mResource;
    }

    @NonNull
    public Type getType() {
        return this.mType;
    }

    @NonNull
    public CreativeType getCreativeType() {
        return this.mCreativeType;
    }

    public void initializeWebView(@NonNull VastWebView webView) {
        Preconditions.checkNotNull(webView);
        if (this.mType == Type.IFRAME_RESOURCE) {
            webView.loadData("<iframe frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" style=\"border: 0px; margin: 0px;\" width=\"" + this.mWidth + "\" height=\"" + this.mHeight + "\" src=\"" + this.mResource + "\"></iframe>");
        } else if (this.mType == Type.HTML_RESOURCE) {
            webView.loadData(this.mResource);
        } else if (this.mType != Type.STATIC_RESOURCE) {
        } else {
            if (this.mCreativeType == CreativeType.IMAGE) {
                webView.loadData("<html><head></head><body style=\"margin:0;padding:0\"><img src=\"" + this.mResource + "\" width=\"100%\" style=\"max-width:100%;max-height:100%;\" /></body>" + "</html>");
            } else if (this.mCreativeType == CreativeType.JAVASCRIPT) {
                webView.loadData("<script src=\"" + this.mResource + "\"></script>");
            }
        }
    }

    @Nullable
    public String getCorrectClickThroughUrl(@Nullable String vastClickThroughUrl, @Nullable String webViewClickThroughUrl) {
        switch (this.mType) {
            case STATIC_RESOURCE:
                if (CreativeType.IMAGE == this.mCreativeType) {
                    return vastClickThroughUrl;
                }
                if (CreativeType.JAVASCRIPT != this.mCreativeType) {
                    return null;
                }
                return webViewClickThroughUrl;
            case HTML_RESOURCE:
            case IFRAME_RESOURCE:
                return webViewClickThroughUrl;
            default:
                return null;
        }
    }
}
