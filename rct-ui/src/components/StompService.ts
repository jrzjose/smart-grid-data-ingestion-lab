import { useState, useRef, useEffect } from 'react';
import { Client } from '@stomp/stompjs';

export function StompService(subCallback) {
    const [status, setStatus] = useState("Disconnected");
    const wsclientRef = useRef(null);
    const url = "ws://localhost:8091/ws-datafeed";
    const pubDest = "/app/agg-upd";
    const pubInitMsg = "ping";
    const subDest = "/topic/agg";    

    useEffect(() => {
        if (wsclientRef.current === null) {
            wsclientRef.current = new Client({ brokerURL: url });
        }

        if (!connected()) {
            console.log("onConnect");
            wsclientRef.current.onConnect = (frame: any) => {
                console.log('Connected: ' + frame);
                setStatus("connected");
                wsclientRef.current.subscribe(subDest, (message: any) => {
                    if (message)
                        subCallback(JSON.parse(message.body))
                });
                publish(pubDest, pubInitMsg);
            };

            wsclientRef.current.onWebSocketError = (error: any) => {
                console.error('WebSocket error', error);
                subCallback('WebSocket error...');
            };

            wsclientRef.current.onStompError = (frame: any) => {
                console.error('WebSocket error: ' + frame.headers['message']);
                console.error('WebSocket error: ' + frame.body);
                subCallback('WebSocket error: ' + + frame.headers['message']);
            };
            wsclientRef.current.activate();
        }
        return () => {
            console.log("closing ws");
            wsclientRef.current.deactivate();
        };
    }, []
    );

    const connected = () => {
        return status[0] === "connected";
    }

    const publish = (dest: any, message: any) => {
        wsclientRef.current.publish({
            destination: dest,
            body: message
        });
    }

    return {
        connected: connected,
        publish: publish
    };
}