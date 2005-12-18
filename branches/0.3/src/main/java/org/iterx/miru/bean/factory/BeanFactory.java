/*
  org.iterx.miru.bean.factory.BeanFactory

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  Copyright (C)2004-2005 Darren Graves <darren@iterx.org>
  All Rights Reserved.  
*/

package org.iterx.miru.bean.factory;

import java.beans.PropertyEditorManager;

import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanProviderListenerAware;
import org.iterx.miru.bean.BeanProviderListener;
import org.iterx.util.SystemUtils;
import org.iterx.util.ArrayUtils;

public abstract class BeanFactory implements BeanProvider,  BeanProviderListenerAware {

    private static final String EDITORS = "org.iterx.miru.bean.editor";
    private static BeanFactory beanFactory;

    private BeanProviderListener[] beanProviderListeners =  new BeanProviderListener[0];

    static {
        String[] paths, clone;

        paths = PropertyEditorManager.getEditorSearchPath();
        clone = new String[paths.length + 1];
        clone[0] = EDITORS;
        System.arraycopy(paths, 0, clone, 1, paths.length);
        PropertyEditorManager.setEditorSearchPath(clone);
    }

    public static BeanFactory getBeanFactory() {

        if(beanFactory == null) {
            String cls;

            if((cls = SystemUtils.getProperty((BeanFactory.class).getName())) != null) {
                try {
                    beanFactory = (BeanFactory) (Class.forName(cls)).newInstance();
                }
                catch(Exception e) {
                    throw new RuntimeException("Failed to create BeanFactory '" + cls + "'.", e);
                }
            }
            else beanFactory = new XmlBeanFactory();
        }
        return beanFactory;
    }

    public static void setBeanFactory(BeanFactory beanFactory) {

        if(beanFactory == null)
            throw new IllegalArgumentException("beanFactory == null");

        BeanFactory.beanFactory = beanFactory;
    }


    public void addListener(BeanProviderListener beanProviderListener) {

        beanProviderListeners =
            (BeanProviderListener[]) ArrayUtils.add(beanProviderListeners,
                                                                   beanProviderListener);
    }


    public void removeListener(BeanProviderListener beanProviderListener) {

        beanProviderListeners =
            (BeanProviderListener[]) ArrayUtils.remove(beanProviderListeners,
                                                       beanProviderListener);
    }

    protected void notifyListeners(BeanProviderListener.Event event, Object data) {

        for(int i = beanProviderListeners.length; i-- > 0; ) {
            beanProviderListeners[i].beanProviderEvent(this, event, data);
        }
    }



}
