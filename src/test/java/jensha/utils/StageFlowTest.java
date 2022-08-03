package jensha.utils;

import jensha._testutils.PScriptFake;
import org.junit.jupiter.api.Test;

import static jensha.utils.StageFlowTest.DummyPipelineStages.*;


class StageFlowTest {

    enum DummyPipelineStages {
        firstStage,
        secondStage,
        thirdStage,
        forthStage,
        fifthStage
    }

    @Test
    void startAndStop() {
        StageFlow<DummyPipelineStages> flow = new StageFlow<>(new PScriptFake());
        flow.fromTo(secondStage, forthStage);

        System.out.println("\n"+ firstStage);
        System.out.println("Start? " + flow.shouldStartHere(firstStage));
        System.out.println("Stop ? " + flow.shouldStop(firstStage));

        System.out.println("\n"+ secondStage);
        System.out.println("Start? " + flow.shouldStartHere(secondStage));
        System.out.println("Stop ? " + flow.shouldStop(secondStage));

        System.out.println("\n"+ thirdStage);
        System.out.println("Start? " + flow.shouldStartHere(thirdStage));
        System.out.println("Stop ? " + flow.shouldStop(thirdStage));

        System.out.println("\n"+ forthStage);
        System.out.println("Start? " + flow.shouldStartHere(forthStage));
        System.out.println("Stop ? " + flow.shouldStop(forthStage));

        System.out.println("\n"+ fifthStage);
        System.out.println("Start? " + flow.shouldStartHere(fifthStage));
        System.out.println("Stop ? " + flow.shouldStop(fifthStage));
    }

    @Test
    void variousConfigs() {
        StageFlow<DummyPipelineStages> flow = new StageFlow<>(new PScriptFake());
        flow.fromTo(secondStage, forthStage);

        System.out.println("\n"+ firstStage);
        System.out.println("Process? " + flow.shouldProcess(firstStage));

        System.out.println("\n"+ secondStage);
        System.out.println("Process? " + flow.shouldProcess(secondStage));

        System.out.println("\n"+ thirdStage);
        System.out.println("Process? " + flow.shouldProcess(thirdStage));

        System.out.println("\n"+ forthStage);
        System.out.println("Process? " + flow.shouldProcess(forthStage));

        System.out.println("\n"+ fifthStage);
        System.out.println("Process? " + flow.shouldProcess(fifthStage));
    }
}
