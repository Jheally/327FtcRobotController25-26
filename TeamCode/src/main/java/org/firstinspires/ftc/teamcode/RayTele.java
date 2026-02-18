// package and imports
// a package defines where our code is stored
package org.firstinspires.ftc.teamcode;

// imports bring in LinearOpMode, motors, servos, etc.
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "ray tele v30")
public class NewTele extends OpMode {

    // declaring hardware
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backRollerMotor;
    private DcMotor frontRollerMotor;
    private CRServo servo;


    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRollerMotor = hardwareMap.get(DcMotor.class, "backRoller");
        frontRollerMotor = hardwareMap.get(DcMotor.class, "leftEncoder");
        servo = hardwareMap.get(CRServo.class, "servo");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        /*
        double drive = +gamepad1.left_stick_y;
        double turn = gamepad1.left_stick_x;

        drive = Range.clip(drive, -1, 1);
        turn = Range.clip(turn, -1, 1);

        frontLeft.setPower(drive + turn);
        backLeft.setPower(drive + turn);
        frontRight.setPower(drive - turn);
        backRight.setPower(drive - turn);

        //uncommented these for now
        frontLeft.setPower(Range.clip(gamepad1.left_stick_x, -1, 1));
        frontRight.setPower(Range.clip(gamepad1.right_stick_y, -1, 1));
        backLeft.setPower(Range.clip(gamepad1.left_stick_y, -1, 1));
        backRight.setPower(Range.clip(gamepad1.right_stick_y, -1, 1));
        */

        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.right_stick_x;
        double turn = gamepad1.left_stick_x;  // rotation


        // Clip values
        forward = Range.clip(forward, -1, 1);
        strafe = Range.clip(strafe, -1, 1);

        // Mecanum math
        frontLeft.setPower(forward + strafe);
        backLeft.setPower(forward - strafe);
        frontRight.setPower(forward - strafe);
        backRight.setPower(forward + strafe);



// Deadzones
        if (Math.abs(forward) < 0.05) forward = 0;
        if (Math.abs(strafe) < 0.05) strafe = 0;
        if (Math.abs(turn) < 0.05) turn = 0;


// Lock forward when strafing
        if (Math.abs(strafe) > 0.1) forward = 0;

// Reduce strafe power
        strafe *= 0.75;

// Mecanum math
        double fl = forward + strafe + turn;
        double bl = forward - strafe + turn;
        double fr = forward - strafe - turn;
        double br = forward + strafe - turn;

// Normalize
        double max = Math.max(
                Math.max(Math.abs(fl), Math.abs(bl)),
                Math.max(Math.abs(fr), Math.abs(br))
        );
        if (max > 1.0) {
            fl /= max;
            bl /= max;
            fr /= max;
            br /= max;
        }

        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);


//trigger is back wheel
//bumper is front
        //if (gamepad1.right_trigger > 0){
        //   backRollerMotor.setPower(-0.75);
        //   frontRollerMotor.setPower(-0.75);
        //backRollerMotor.setPower(-1);
        //   frontRollerMotor.setPower(-1);
        //} else {
        //   backRollerMotor.setPower(0);
        //   //backRollerMotor.setPower(0);
        //   frontRollerMotor.setPower(0);
        //}

        //if (gamepad1.right_bumper){
        //backRollerMotor.setPower(0.75);
        //frontRollerMotor.setPower(0.75);
        //backRollerMotor.setPower(-1);
        //frontRollerMotor.setPower(1);
        //} else {
        //backRollerMotor.setPower(0);
        //frontRollerMotor.setPower(0);
        //}


        if (gamepad1.right_bumper){
            //backRollerMotor.setPower(-1);
            frontRollerMotor.setPower(-1);
        } else {
            //backRollerMotor.setPower(0);
            frontRollerMotor.setPower(0);
        }

        if (gamepad1.right_trigger>0){
            backRollerMotor.setPower(-1);
            //frontRollerMotor.setPower(1);
        } else {
            backRollerMotor.setPower(0);
            //frontRollerMotor.setPower(0);
        }

        //to move the server to help it get closer
        if (gamepad1.left_bumper){
            servo.setPower(-1);
        }
        //else {
        // servo.setPower(0);
        //}

        if(gamepad1.left_trigger>0){
            servo.setPower(1);
        }
        //else {
        //    servo.setPower(0);
        //}
    }
}


