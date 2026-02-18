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

@TeleOp (name = "gg and victoria tele w/ re telemetry")
public class teleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotorEx backRollerMotor = hardwareMap.get(DcMotorEx.class, "backRoller");
        DcMotor frontRollerMotor = hardwareMap.get(DcMotor.class, "leftEncoder");
        IMU imu = hardwareMap.get(IMU.class, "imu");
        CRServo servo = hardwareMap.get(CRServo.class, "servo");
        boolean outtakeSlowDown = false;
        boolean outtakeHasRun = false;

        //outtake pf coefficients
        double P = 51;
        double F = 13.4050;

        //outtake power => 0.56 6000 RPM in ticks/rev
        int desiredOutput = 1568;
        int slowDownOutput = 900;

        //outtake
        backRollerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //outtake pidf
        PIDFCoefficients pidf = new PIDFCoefficients(
                P,
                0,
                0,
                F
        );
        backRollerMotor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidf);

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //Timer
        ElapsedTime timer = new ElapsedTime();

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                //Okay, if you want the direction of the robot's front to change, make the RIGHT a LEFT, and vice versa. this is supposed to change the direction that the robot's front is, pleaseeeeeee keep this in mind!
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


            //  OUTTAKE/INTAKE LOGIC
            if (gamepad1.right_trigger > .1) {
                //trigger goes
                backRollerMotor.setVelocity(desiredOutput);
                outtakeSlowDown = false;
                outtakeHasRun = true;
            } else {
                //trigger released
                if (outtakeHasRun) {
                    if (!outtakeSlowDown) {
                        timer.reset();
                        outtakeSlowDown = true;
                    }

                    //slow down time
                    if (timer.seconds() < 1.0) {
                        backRollerMotor.setVelocity(slowDownOutput);
                    } else {
                        backRollerMotor.setVelocity(0);
                        outtakeHasRun = false;
                        outtakeSlowDown = false;
                    }
                }

                if (gamepad1.right_bumper) {
                    frontRollerMotor.setPower(-0.95);
                } else {
                    frontRollerMotor.setPower(0);
                }

                if (gamepad1.bWasPressed()) {
                    imu.resetYaw();
                }
//dont touch servo, servo good
                if (gamepad1.left_bumper) {
                    servo.setPower(-1);
                }

                if (gamepad1.left_trigger > .1) {
                    servo.setPower(.5);
                }

                if (Math.abs(x) < 0.05) x = 0;
                if (Math.abs(y) < 0.05) y = 0;
                if (Math.abs(r) < 0.05) r = 0;
            }
        }
    }
}