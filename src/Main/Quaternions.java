package Main;

import Quaternion.*;

//***************************************************************************************************//
// Created by Mikayla Fulmer                                                                         //
// Last edited 7-24-25                                                                               //
//                                                                                                   //
// This programs tests using quaternions to rotate points in 3d space.                               //
// Outputs points, copy and paste into https://www.desmos.com/3d to visualize.                       //
//***************************************************************************************************//
public class Quaternions
{
    static public void main(String[] args)
    {
        //Some sample points to test
        //VectorR3 Point = new VectorR3(1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));
        //VectorR3 Point = new VectorR3(1.0, 1.0, 1.0);
        //VectorR3 Point = new VectorR3(-2.9, 0.0, 1.4);
        //VectorR3 Point = new VectorR3(3.0, 3.0, 0.0);
        VectorR3 Point = new VectorR3(3.0, 0.0, 3.0);


        //testing smooth rotate
        int NumMoves = 48;
        VectorR3[] Moves;
        //VectorR3 RotAxis = new VectorR3(2.0, -4.0, 2.0*Math.sqrt(3.0));
        //VectorR3 RotAxis = new VectorR3(Math.PI, Math.E, Math.sqrt(2.0));
        //VectorR3 RotAxis = new VectorR3(-1.0, 1.0, 1.0);
        VectorR3 RotAxis = new VectorR3(0.0, -1.0, 1.0);
        double RotAngle = Math.PI;
        VectorR3 TransVector = new VectorR3(0.0, 1.0, -2.0);

        System.out.printf("(%.4f, %.4f, %.4f)\n", Point.GetX(), Point.GetY(), Point.GetZ());
        Moves = Rotator.SmoothRotate(Point, RotAngle, RotAxis, NumMoves);
        for(int i = 0; i < NumMoves; i++)
        {
            Point.SetAll(Moves[i].Add(TransVector));
            System.out.printf("(%.4f, %.4f, %.4f)\n", Point.GetX(), Point.GetY(), Point.GetZ());
        }
    }
}
