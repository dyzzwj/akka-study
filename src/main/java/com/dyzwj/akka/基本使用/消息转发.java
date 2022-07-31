package com.dyzwj.akka.基本使用;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class 消息转发 {

    static class ChildActor extends AbstractActor{

        public static Props props(){
            return Props.create(ChildActor.class,ChildActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> System.out.println(msg )).build();
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
                //可以自定义小的发送者
//                childActor.tell(msg,getSelf());
                childActor.forward(msg,getContext());
            }).build();
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef actorRef = actorSystem.actorOf(ParentActor.props(),"parentActor");
        actorRef.tell("hello world",ActorRef.noSender());

    }


}
