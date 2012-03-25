package nc.uap.portal.container.portlet;

import javax.portlet.CacheControl;

public class CacheControlImpl implements CacheControl
    {
        private String eTag;
        private int expirationTime;
        private boolean publicScope;
        private boolean cachedContent;
        
        public CacheControlImpl()
        {
        }

        public boolean useCachedContent()
        {
            return cachedContent;
        }

        public String getETag()
        {
            return this.eTag;
        }

        public int getExpirationTime()
        {
            return expirationTime;
        }

        public boolean isPublicScope()
        {
            return publicScope;
        }

        public void setETag(String eTag)
        {
            this.eTag = eTag;
        }

        public void setExpirationTime(int expirationTime)
        {
            this.expirationTime = expirationTime;
        }

        public void setPublicScope(boolean publicScope)
        {
            this.publicScope = publicScope;
        }

        public void setUseCachedContent(boolean cachedContent)
        {
            this.cachedContent = cachedContent;
        }
    }