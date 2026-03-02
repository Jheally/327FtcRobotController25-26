package org.firstinspires.ftc.teamcode;

//importing the needed classes from FTC SDK
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;//imports the timer

@TeleOp(name = "scrimtele")
public class NewTele extends OpMode {

    //drive motors variables
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    //mechanism variables
    private DcMotorEx backRollerMotor;
    private DcMotor frontRollerMotor;
    private CRServo servo;

    //constants variables
    static final double TICKS_PER_REV = 28.0;
    static final double TARGET_RPM = 3200;   // adjust if needed
    static final double SHOOTER_POWER = -1.0;
    static final double SERVO_PULSE_SECONDS = 0.4; // servo pulse duration

    //state variables
    private boolean singleShotActive = false;
    private boolean hasShot = false;
    private ElapsedTime pulseTimer = new ElapsedTime();//creating a timer variable

    @Override
    public void init() {

        //map the motors/servo to the names in the robot configuration
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);//reverses the motor

        backRollerMotor = hardwareMap.get(DcMotorEx.class, "backRoller");
        backRollerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRollerMotor = hardwareMap.get(DcMotor.class, "leftEncoder");

        servo = hardwareMap.get(CRServo.class, "servo");
    }

    @Override
    public void loop() {

        /* ---------------- DRIVE ---------------- */
        //to move the robot
        double forward = -gamepad1.left_stick_y;//forward/backward
        double strafe  =  gamepad1.right_stick_x;//left/right
        double turn    =  gamepad1.left_stick_x;//shafting

        //combine inputsnto calculate power for each wheel
        double fl = forward + strafe + turn;
        double bl = forward - strafe + turn;
        double fr = forward - strafe - turn;
        double br = forward + strafe - turn;

        //makes sure no wheel goes above 100%
        double max = Math.max(Math.max(Math.abs(fl), Math.abs(bl)),
                              Math.max(Math.abs(fr), Math.abs(br)));

        if (max > 1.0) {
            fl /= max; bl /= max; fr /= max; br /= max;
        }

        //sends power to motors
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);

        /* ---------------- FRONT ROLLER ---------------- */
        //runs the front roller if right bumper is pressed
        frontRollerMotor.setPower(gamepad1.right_bumper ? -1 : 0);

        /* ---------------- RPM ---------------- */
        //gets shooter motor speed in ticks per second
        double ticksPerSecond = Math.abs(backRollerMotor.getVelocity()); 
        //converts ticks per second to RPM
        double rpm = (ticksPerSecond / TICKS_PER_REV) * 60.0;

        //chekcs if triggers are pressed
        boolean rightTrigger = gamepad1.right_trigger > 0.1;
        boolean leftTrigger  = gamepad1.left_trigger > 0.1;

        /* ---------- RIGHT TRIGGER: SINGLE SHOT ---------- */
        
        if (rightTrigger && !leftTrigger) {
            singleShotActive = true;//starts single shot mode
        }

        if (singleShotActive) {
            // run roller
            backRollerMotor.setPower(SHOOTER_POWER);

            // check RPM and pulse servo once
            if (rpm >= TARGET_RPM && !hasShot) {
                servo.setPower(1);
                pulseTimer.reset();
                hasShot = true;
            }

            // stop servo after pulse duration
            if (hasShot && pulseTimer.seconds() >= SERVO_PULSE_SECONDS) {
                servo.setPower(0);
            }
        }

        // Reset single-shot when trigger released
        if (!rightTrigger) {
            singleShotActive = false;
            hasShot = false;
            servo.setPower(0);
        }

        /* ---------- LEFT TRIGGER: CONTINUOUS FIRE ---------- */
        if (leftTrigger) {
            //keeps shooter running
            backRollerMotor.setPower(SHOOTER_POWER);

            //if rpm is greater or equal to target speed, shoot the servo
            if (rpm >= TARGET_RPM) {
                servo.setPower(1);
            } else {
                servo.setPower(0);
            }
        }

        /* ---------- STOP WHEN NO TRIGGERS ---------- */
        if (!rightTrigger && !leftTrigger) {
            backRollerMotor.setPower(0);//reset/rests the servo and roller
            servo.setPower(0);
        }

        /* ---------------- TELEMETRY ---------------- */
        //to be able to veiw these info in the driver hub
        telemetry.addData("Ticks/sec", ticksPerSecond);
        telemetry.addData("RPM", rpm);
        telemetry.addData("Right Trigger Active", rightTrigger);
        telemetry.addData("Left Trigger Active", leftTrigger);
        telemetry.addData("Servo Pulse Running", hasShot && pulseTimer.seconds() < SERVO_PULSE_SECONDS);
        telemetry.update();
    }
}
