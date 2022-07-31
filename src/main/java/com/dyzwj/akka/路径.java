package com.dyzwj.akka;

import akka.actor.*;

public class 路径 {

    static class SimpleActor extends AbstractActor{

        public static Props props(){
            return Props.create(SimpleActor.class,SimpleActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> System.out.println(msg + ":" + getSelf())).build();
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef actorRef1 = actorSystem.actorOf(SimpleActor.props(), "simpleActor1");
        ActorRef actorRef2 = actorSystem.actorOf(SimpleActor.props(), "simpleActor2");
        System.out.println(actorRef1);//Actor[akka://sys/user/simpleActor1#-1243504679]

        //通过路径获取actor
        ActorSelection actorSelection = actorSystem.actorSelection("akka://sys/user/simpleActor1");
//        actorSelection.tell("hello world",ActorRef.noSender());

        //匹配符查找
        ActorSelection actorSelection1 = actorSystem.actorSelection("akka://sys/user/simpleActor*");
        actorSelection1.tell("hello world",ActorRef.noSender());


    }
}
