package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer; // Use Pedro's Timer class


@Autonomous(name = "Pedro Front Shoot Auto", group = "Autonomous")
@Configurable // Panels
public class PedroFrontShootAuto extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    private Timer pathTimer; // We'll use this to track time in each state

    public void setPathState(int state){
        pathState = state;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        pathTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        //must match start pose to path 1
        follower.setStartingPose(new Pose(56.000, 11.371, Math.toRadians(140)));

        paths = new Paths(follower); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        telemetry.addData("Timer", pathTimer.getElapsedTimeSeconds());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);

        telemetry.addData("Path State", pathState);
        telemetry.addData("Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
    }


    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.000, 11.371),

                                    new Pose(60.700, 19.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(140), Math.toRadians(119))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.700, 19.500),

                                    new Pose(13.500, 15.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(119), Math.toRadians(119))

                    .build();

            Path3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(13.500, 15.000),

                                    new Pose(53.500, 22.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(119), Math.toRadians(119))

                    .build();
        }
    }


    public void autonomousPathUpdate() {
        switch(pathState){
            case 0:
                //move up a bit aka path 1
                follower.followPath(paths.Path1);
                setPathState(1);
                break;
            case 1:
                //wait for path1 to finish
                if (!follower.isBusy()){
                    //shoot preloads
                    //TODO: FLYWHEEL SHOOT 3 PRELOADS
                    setPathState(2);
                }
                break;
            case 2:
                //move to human player
                follower.followPath(paths.Path2);
                setPathState(3);
                break;
            case 3:
                //wait for path 2 to finish
                if (!follower.isBusy()){
                    //get artifacts from human player
                    setPathState(4);
                }
                break;
            case 4:
                //wait for path3 to finish
                if (pathTimer.getElapsedTimeSeconds()>6.0){
                    follower.followPath(paths.Path3);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()){
                    //TODO: 2ND ROUND OF ARTIFACTS
                    setPathState(6);
                }
                break;
            case 6:
                //done
                break;

        }
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
    }
}