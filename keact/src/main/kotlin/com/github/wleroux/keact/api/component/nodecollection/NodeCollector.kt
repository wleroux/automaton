package com.github.wleroux.keact.api.component.nodecollection

import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class NodeCollector: NodeCollectionBuilder() {
    fun build() =
            nodes
}