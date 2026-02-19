/*
 * Copyright (c) 2018-2022 NetFoundry Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hanzozt.net

import org.hanzozt.Ziti
import org.hanzozt.ZitiContext
import org.hanzozt.impl.ZitiContextImpl
import org.hanzozt.net.nio.AsychChannelSocket
import org.hanzozt.net.nio.AsyncSocketImpl
import org.hanzozt.util.Logged
import org.hanzozt.util.ZitiLog
import java.net.*
import java.nio.channels.AsynchronousSocketChannel
import javax.net.SocketFactory

/**
 *
 */
internal class ZitiSocketFactory(val contexts: () -> Collection<ZitiContext> ): SocketFactory() {

    constructor(ztx: ZitiContext): this(contexts = { setOf(ztx) })

    constructor(): this(contexts = { Ziti.getContexts() })

    class ZitiConnector(val contexts: () -> Collection<ZitiContext>): AsyncSocketImpl.Connector, Logged by ZitiLog() {

        companion object {
            val Default = ZitiConnector { Ziti.getContexts() }
        }

        override fun connect(addr: SocketAddress, timeout: Int): AsynchronousSocketChannel {
            val sockAddr = addr as InetSocketAddress
            for (ctx in contexts()) {
                val ctxImpl = ctx as ZitiContextImpl
                try {
                    val s = ctxImpl.getService(sockAddr)
                    if (s != null)
                        return doConnect(ctx.open(), sockAddr, timeout)
                } catch (ex: Exception) {
                    w{"${ctx.name()}: $ex"}
                }
            }

            i{"no ZitiContext provides service for $addr"}
            throw ConnectException()
        }
    }

    override fun createSocket(): Socket = AsychChannelSocket(AsyncSocketImpl(ZitiConnector(this.contexts)))

    override fun createSocket(p0: String?, p1: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(p0: String?, p1: Int, p2: InetAddress?, p3: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(p0: InetAddress?, p1: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(p0: InetAddress?, p1: Int, p2: InetAddress?, p3: Int): Socket {
        error("not implemented")
    }
}