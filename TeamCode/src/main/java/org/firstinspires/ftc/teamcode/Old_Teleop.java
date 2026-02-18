// package and imports

// a package defines where our code is stored
package org.firstinspires.ftc.teamcode;

// imports bring in LinearOpMode, motors, servos, etc.
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "pot")
public class Old_Teleop extends LinearOpMode {

    // declaring hardware
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;


    @Override
    public void runOpMode() {
        // declaring variables

        float y;
        double x;
        float rx;
        double m;
        double b;
        double denominator;
        float triggerR;
        boolean bumperR;

        // naming all motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");

        // starting the robot
        waitForStart();

        // reversing motors so wheels turn in the right directions
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        // runs loop as long as op mode is active
        if (opModeIsActive()) {

            // reads joystick/gamepad input (multipliers scale them down so it is easier to control)
            while (opModeIsActive()) {
                y = -gamepad1.left_stick_y;
                x = gamepad1.right_stick_x * 0.7;
                rx = gamepad1.left_stick_x;
                m = -(gamepad2.right_stick_y * 0.5);
                b = gamepad2.left_stick_y * 0.65;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx), Math.abs(m))), 1));


                // moving mecanum wheels based on input
                frontLeftMotor.setPower((y + x + rx) / denominator);
                backLeftMotor.setPower(((y - x) + rx) / denominator);
                frontRightMotor.setPower(((y - x) - rx) / denominator);
                backRightMotor.setPower(((y + x) - rx) / denominator);

            }
        }
    }
}
