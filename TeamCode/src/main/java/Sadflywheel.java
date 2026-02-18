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

@TeleOp (name = "sad flywheel v20")
public class Sadflywheel extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotorEx backRollerMotor = hardwareMap.get(DcMotorEx.class, "backRoller");
        backRollerMotor.setDirection(DcMotorEx.Direction.REVERSE);
        DcMotor frontRollerMotor = hardwareMap.get(DcMotor.class, "leftEncoder");
        IMU imu = hardwareMap.get(IMU.class, "imu");
        CRServo servo = hardwareMap.get(CRServo.class, "servo");

        boolean outtakeSlowDown = false;
        boolean outtakeHasRun = false;

        //outtake pf coef
        double P = 51;
        double F = 13.4050;

        //outtake power => 0.56 * 6000 rpm in ts/rev .. 1568
        int desiredOutput = 1500;
        int slowDownOutput = 900;

        //outtake settings
        backRollerMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
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

        //timer
        ElapsedTime timer = new ElapsedTime();

        //IMU initialization
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

            //OUTTAKE LOGIC
            if (gamepad2.right_trigger > 0.1) {
                backRollerMotor.setVelocity(desiredOutput);
                outtakeSlowDown = false;
                outtakeHasRun = true;
            } else {
                if (outtakeHasRun){
                    if (!outtakeSlowDown){
                        timer.reset();
                        outtakeSlowDown = true;
                    }
                    if (timer.seconds()<1.0){
                        backRollerMotor.setVelocity(slowDownOutput);
                    } else {
                        backRollerMotor.setVelocity(0);
                        outtakeHasRun = false;
                        outtakeSlowDown = false;
                    }
                }
            }

            if (gamepad2.right_bumper){
                frontRollerMotor.setPower(-0.5);
            } else {
                frontRollerMotor.setPower(0);
            }


            if (gamepad2.yWasPressed()) {
                backRollerMotor.setVelocity(0);
            }


            if (gamepad1.b) {
                imu.resetYaw();
            }
//dont touch servo, servo good

            //makes front roller go back
            if (gamepad2.left_bumper){
                frontRollerMotor.setPower(.65);
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


            telemetry.addData("Flywheel vel", backRollerMotor.getVelocity());
            telemetry.update();
        }

    }


}

