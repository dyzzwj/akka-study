package com.dyzwj.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class HelloWorld {

    static class HelloActor extends AbstractActor{
        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny(System.out::println).build();
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef helloActor = actorSystem.actorOf(Props.create(HelloActor.class), "helloActor");
        helloActor.tell("Hello World",ActorRef.noSender());
    }
}
