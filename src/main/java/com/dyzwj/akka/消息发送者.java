package com.dyzwj.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class 消息发送者 {

    static class ChildActor extends AbstractActor{

        public static Props props(){
            return Props.create(ChildActor.class,ChildActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                System.out.println(msg + ";" + getSender());
            }).build();
        }
    }

    static class ParentActor extends AbstractActor{
        public static Props props(){
            return Props.create(ParentActor.class,ParentActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                ActorRef childActor = getContext().actorOf(ChildActor.props(), "childActor");
//                childActor.tell(msg,getSelf());
                //转发 消息发送者是源消息的发送者
                childActor.forward(msg,getContext());
            }).build();
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef parentACtor = actorSystem.actorOf(ParentActor.props(), "parentACtor");
        parentACtor.tell("hello world",ActorRef.noSender());

    }

}
