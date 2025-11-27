import { signal, Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class StompService {
    private wsclientRef: any;
    private state: any = signal("disconnected");
    private sharedDataSubject = new BehaviorSubject<any>([]);
    public sharedData$: Observable<any> = this.sharedDataSubject.asObservable();

    private url: string = "ws://localhost:8091/ws-datafeed";
    private pubDest: string = "/app/agg-upd";
    private pubInitMsg: string = "ping";
    private subDest: string = "/topic/agg";

    public connect(): void {
        console.log('Connecting...');
        this.wsclientRef = new Client({ brokerURL: this.url });
        this.wsclientRef.onConnect = (frame: any) => {
            console.log('Connected: ' + frame);
            this.state.set("connected");
            this.wsclientRef.subscribe(this.subDest, (message: any) => {
                if (message) {
                    this.sharedDataSubject.next(JSON.parse(message.body))
                }
            });

            this.publish(this.pubDest, this.pubInitMsg);
        };

        this.wsclientRef.onWebSocketError = (error: any) => {
            console.error('WebSocket error', error);
            this.sharedDataSubject.next('WebSocket error...');
        };

        this.wsclientRef.onStompError = (frame: any) => {
            console.error('WebSocket error: ' + frame.headers['message']);
            console.error('WebSocket error: ' + frame.body);
            this.sharedDataSubject.next('WebSocket error: ' + + frame.headers['message']);
        };

        this.wsclientRef.activate();
    }

    public connected(): boolean {
        return this.state() == "connected";
    }

    public publish(dest: any, message: any): void {
        this.wsclientRef.publish({
            destination: dest,
            body: message
        });
    }

    public disconnect(): void {
        if (this.wsclientRef && this.wsclientRef.connected) {
            this.wsclientRef.disconnect(() => {
                console.log('Disconnected');
            });
        }
    }
}