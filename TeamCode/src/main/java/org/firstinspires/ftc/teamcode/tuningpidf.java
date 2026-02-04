package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class tuningpidf extends OpMode {
    public double highVelocity = 1500;
    public double lowVelocity = 900;

    double curTargetVelocity = highVelocity;

    double F = 0;
    double P = 0;
    double[] stepSizes = {10.0, 1.0, 0.1, .001, .0001};
    int stepIndex = 1;
    DcMotorEx backRollerMotor;
    @Override
    public void init() {
        backRollerMotor = hardwareMap.get(DcMotorEx.class, "backRoller");
        backRollerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRollerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRollerMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        backRollerMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init complete");
        double F = 200;
        PIDFCoefficients pidf = new PIDFCoefficients(
                70.0,
                0.0,
                2,
                F
        );

        backRollerMotor.setVelocityPIDFCoefficients(
                70.0,
                0.0,
                2,
                F);

        double targetRPM = -6000;
        double ticksPerRev = 28;
        double targetTicksPerSecond = (targetRPM * ticksPerRev) / 60.0;
    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVelocity) {
                curTargetVelocity = lowVelocity;
            } else { curTargetVelocity = highVelocity; }
        }

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }

        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }

      /*  if (gamepad2.right_trigger > 0.1) {
            backRollerMotor.setVelocity(targetTicksPerSecond);
        } else {
            backRollerMotor.setVelocity(0);
        } */



        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        backRollerMotor.setVelocity(curTargetVelocity);

        double curVelocity = backRollerMotor.getVelocity();
        double error = curTargetVelocity - curVelocity;

    telemetry.addData("Target Velocity", curTargetVelocity);
    telemetry.addData("Current Velocity", "%.2f", curVelocity);
    telemetry.addData("Error", "%.2f", error);
    telemetry.addLine("----------------");
    telemetry.addData("Tuning P", "%.4f (D-Pad U/D)", P);
    telemetry.addData("Tuning F", "%.4f (D-Pad L/R)", F);
    telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIndex]);

    }


}
