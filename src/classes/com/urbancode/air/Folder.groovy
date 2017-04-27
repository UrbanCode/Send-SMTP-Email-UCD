/**
 * © Copyright IBM Corporation 2014.  
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

package com.urbancode.air

import java.util.Map;


class Folder {
    def id
    def name
    def parentId
    def Map<String, Folder> children = new HashMap<String, Folder>()
    
    public Folder(id, name, parentId) {
        this.id = id
        this.name = name
        this.parentId = parentId
    }

    public boolean equals(Object o) {
        if (o instanceof Folder && o != null) {
            return id.equals(o.id)
        }
        else {
            return false
        }
    }

    public String toString() {
        return "[id:$id, name:$name, parent:$parentId, children:${children.keySet()}]"
    }
}

