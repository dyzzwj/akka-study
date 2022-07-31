package com.dyzwj.akka.基本使用;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class 行为切换 {

    static class SimpleActor extends AbstractActor{

        private int count;
        Receive receivec = receiveBuilder().matchAny((msg) -> {
            if("unbecome".equals(msg)){
                getContext().unbecome();
            }else {
                System.out.println(msg + ":" + "优惠100");
            }
        }).build();

        Receive receiveb = receiveBuilder().matchAny((msg) -> {
            if("become".equals(msg)){
                //abc
//                getContext().become(receivec,false);//8:优惠500
                //ac
                getContext().become(receivec,true);//8:优惠1000
            }else {
                System.out.println(msg + ":" + "优惠500");

            }
        }).build();



        public static Props props(){
            return Props.create(SimpleActor.class,SimpleActor::new);
        }

        @Override
        public Receive createReceive() {
            Receive receivea = receiveBuilder().matchAny((msg) -> {
                count++;
                System.out.println(msg + ":" + "优惠1000");
                if(count == 3){
                    //切换逻辑
                    //第二个参数discardOld:  是否丢弃老的  a -> b   如果 b-> c，discard传false 那么顺序就是abc 此时在c里调unbecome，回到b
                    //如果 b-> c，discard传false 那么顺序就是ac 此时在c里调unbecome，回到a
                    getContext().become(receiveb);
                }
            }).build();

            return receivea;
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef actorRef = actorSystem.actorOf(SimpleActor.props(), "simpleActor");
        actorRef.tell("1",ActorRef.noSender());
        actorRef.tell("2",ActorRef.noSender());
        actorRef.tell("3",ActorRef.noSender());
        actorRef.tell("4",ActorRef.noSender());
        actorRef.tell("5",ActorRef.noSender());
        actorRef.tell("become",ActorRef.noSender());
        actorRef.tell("6",ActorRef.noSender());
        actorRef.tell("7",ActorRef.noSender());
        actorRef.tell("unbecome",ActorRef.noSender());
        actorRef.tell("8",ActorRef.noSender());  //8:优惠500   //

    }
}
