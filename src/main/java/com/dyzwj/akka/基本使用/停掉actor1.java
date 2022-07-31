package com.dyzwj.akka.基本使用;

import akka.actor.*;

/**
 * 停止 Actor 大致有三种方式，它们分别是：
 * ● 调用 ActorSystem 或者 getContext() 的 stop 方法；
 * ● 给 Actor 发送一个 PoisonPill （毒丸）消息；
 * ● 给 Actor 发送一个 Kill 的消息，此时会抛出ActorKilledException 异常，并上报到父级 supervisor 处理。
 */
public class 停掉actor1 {

    static class SimpleActor extends AbstractActor{

        public static Props props(){
            return Props.create(SimpleActor.class,SimpleActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                System.out.println(msg);
            }).build();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef actorRef = actorSystem.actorOf(SimpleActor.props(), "simpleActor");
        actorRef.tell("1",ActorRef.noSender());
        actorRef.tell("2",ActorRef.noSender());

        Thread.sleep(1000);
        //停掉actor  不建议
        actorSystem.stop(actorRef);
        Thread.sleep(1000);

        actorRef.tell("3",ActorRef.noSender());

    }
}
