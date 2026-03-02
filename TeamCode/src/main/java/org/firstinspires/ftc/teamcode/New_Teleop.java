// package and imports
// a package defines where our code is stored
package org.firstinspires.ftc.teamcode;

// imports bring in LinearOpMode, motors, servos, etc.
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "ray tele v13")
public class NewTele2 extends OpMode {

    // declaring hardware
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backRollerMotor;
    private DcMotor frontRollerMotor;


    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRollerMotor = hardwareMap.get(DcMotor.class, "rollerMotor");
        frontRollerMotor = hardwareMap.get(DcMotor.class, "leftEncoder");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double drive = gamepad1.left_stick_y * 1.1;
        double turn = -gamepad1.left_stick_x * 1.1;
        double strafe = gamepad1.right_stick_x * 1.1;

        drive = Range.clip(drive, -1, 1);
        turn = Range.clip(turn, -1, 1);
        strafe = Range.clip(strafe, -1, 1);

        frontLeft.setPower(drive + turn + strafe);
        backLeft.setPower(drive + turn - strafe);
        frontRight.setPower(drive - turn - strafe);
        backRight.setPower(drive - turn + strafe);

        //frontLeft.setPower(Range.clip(gamepad1.left_stick_x, -1, 1));
        //frontRight.setPower(Range.clip(gamepad1.right_stick_y, -1, 1));
        //backLeft.setPower(Range.clip(gamepad1.left_stick_y, -1, 1));
        //backRight.setPower(Range.clip(gamepad1.right_stick_y, -1, 1));

        if (gamepad1.right_trigger > 0){
            backRollerMotor.setPower(-1);
            frontRollerMotor.setPower(-1);
        } else {
            backRollerMotor.setPower(0);
            frontRollerMotor.setPower(0);
        }

        if (gamepad1.right_bumper){
            backRollerMotor.setPower(1);
            frontRollerMotor.setPower(1);
        } else {
            backRollerMotor.setPower(0);
            frontRollerMotor.setPower(0);
        }
    }
}
