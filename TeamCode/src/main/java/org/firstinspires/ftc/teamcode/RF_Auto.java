package org.firstinspires.ftc.TeamCode;
// where it is located
import com qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
public class RF_Auto {

    //dclaring variables
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private CRServo servoRotate;

    @Override
    public void runOpMode() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        servoRotate = hardwareMap.get(CRServo.class, "servoRotate");

        waitForStart();
        // go striaght back
        frontLeftMotor.setPower(-0.5);
        backLeftMotor.setPower(-0.5);
        frontRightMotor.setPower(-0.5);
        backRightMotor.setPower(-0.5);
        sleep(560);
        // turning to right
        frontLeftMotor.setPower(0.5);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0.5);
        backRightMotor.setPower(0);
        sleep(30);
    }
}
