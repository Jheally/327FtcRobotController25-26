package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp (name = "gg and victoria tele")
public class telechopped extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotorEx backRollerMotor = hardwareMap.get(DcMotorEx.class, "backRoller");
        backRollerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRollerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        DcMotor frontRollerMotor = hardwareMap.get(DcMotor.class, "leftEncoder");
        IMU imu = hardwareMap.get(IMU.class, "imu");
        CRServo servo = hardwareMap.get(CRServo.class, "servo");
        double F = 15;
        PIDFCoefficients pidf = new PIDFCoefficients(
                150,
                0,
                5,
                F
        );

        backRollerMotor.setVelocityPIDFCoefficients(
               150,
                0,
                5,
                F
        );

        double targetRPM = -3360;
        double ticksPerRev = 28;
        double targetTicksPerSecond = (targetRPM * ticksPerRev) / 60.0;

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);


        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y * .7;
            //forward
            double x = gamepad1.left_stick_x * .7;
            //turn
            double r = gamepad1.right_stick_x * .7;
            //strafe
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(r), 1);
            double frontLeftPower = (rotY + rotX + r) / denominator;
            double backLeftPower = (rotY - rotX + r) / denominator;
            double frontRightPower = (rotY - rotX - r) / denominator;
            double backRightPower = (rotY + rotX - r) / denominator;



            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);


             if (gamepad2.right_trigger > 0.1) {
                backRollerMotor.setVelocity(targetTicksPerSecond);
            } else {
                backRollerMotor.setVelocity(0);
            }

            if (gamepad2.right_bumper){
                frontRollerMotor.setPower(-0.5);
            } else {
                frontRollerMotor.setPower(0);
            }

            if (gamepad2.xWasPressed()) {
                backRollerMotor.setVelocity(targetTicksPerSecond);
            } else {
                backRollerMotor.setVelocity(0);
            }

            if (gamepad2.yWasPressed()) {
                backRollerMotor.setVelocity(0);
            }


            if (gamepad1.b) {
                imu.resetYaw();
            }
//dont touch servo, servo good
            if (gamepad2.left_bumper){
                frontRollerMotor.setPower(.5);
            }

            if (gamepad2.left_trigger > 0.1) {
                servo.setPower(.7);
                sleep(500);
                frontRollerMotor.setPower(.5);
                sleep(1500);
            } else {
                servo.setPower(-1);
            }


            if (Math.abs(x) < 0.05) x = 0;
            if (Math.abs(y) < 0.05) y = 0;
            if (Math.abs(r) < 0.05) r = 0;

            telemetry.addData("Flywheel Target TPS", targetTicksPerSecond);
            telemetry.addData("Flywheel Actual TPS", backRollerMotor.getVelocity());
            telemetry.update();
        }


    }


}

