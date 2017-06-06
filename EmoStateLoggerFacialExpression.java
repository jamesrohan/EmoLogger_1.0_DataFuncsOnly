import com.emotiv.Iedk.*;
import com.emotiv.Iedk.EmoState.IEE_InputChannels_t;
import com.sun.jna.*;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;

public class EmoStateLoggerFacialExpression {

	static Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
	static Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();
	static FileWrite OutPutFile_Data = new FileWrite("FacialExprDataLog",".csv");
	static int state =0; static IntByReference userID = new IntByReference(1);
	
	static String[] SignalStrengthStr = {"No Signal", "Bad", "Fair", "Good", "Excellent"};
	
	public static void main(String[] args) {
			if(Check_To_See_If_Emotiv_ConnectionIsEstablished() == true){
				OutPutFile_Data.CreatePrintWriter();
				OutPutFile_Data.WriteFacialExprHeader();
				//OutPutFile_Data.Write_To_File("Hi"+","+"So");
				//OutPutFile_Data.Close_Writer();
				//System.out.println("Done");
				
					while(true){
						state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);
							if (state == EdkErrorCode.EDK_OK.ToInt()){
								
								int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
								Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);
								
								if(eventType == Edk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()){
									Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(eEvent, eState);
									
									
									
									IntByReference chargeLevel = new IntByReference(0); IntByReference maxChargeLevel = new IntByReference(0);
									Float timestamp = EmoState.INSTANCE.IS_GetTimeFromStart(eState);
									EmoState.INSTANCE.IS_GetBatteryChargeLevel(eState, chargeLevel, maxChargeLevel);
									int SignalStrength = EmoState.INSTANCE.IS_GetWirelessSignalStatus(eState);
									int IS_GetHeadsetOn = EmoState.INSTANCE.IS_GetHeadsetOn(eState);
									
									
									
									IntByReference pfAvailableOut = new IntByReference(0); 
									Edk.INSTANCE.IEE_FacialExpressionGetTrainedSignatureAvailable(userID.getValue(), pfAvailableOut);
									
									
									
									int IS_FacialExpressionIsBlink = EmoState.INSTANCE.IS_FacialExpressionIsBlink(eState);
									int IS_FacialExpressionIsLeftWink= EmoState.INSTANCE.IS_FacialExpressionIsLeftWink(eState);
									int IS_FacialExpressionIsRightWink = EmoState.INSTANCE.IS_FacialExpressionIsRightWink(eState);
									int IS_FacialExpressionIsEyesOpen = EmoState.INSTANCE.IS_FacialExpressionIsEyesOpen(eState);
									int IS_FacialExpressionIsLookingUp = EmoState.INSTANCE.IS_FacialExpressionIsLookingUp(eState);
									int IS_FacialExpressionIsLookingDown = EmoState.INSTANCE.IS_FacialExpressionIsLookingDown(eState);
									int IS_FacialExpressionIsLookingLeft = EmoState.INSTANCE.IS_FacialExpressionIsLookingLeft(eState);
									int IS_FacialExpressionIsLookingRight = EmoState.INSTANCE.IS_FacialExpressionIsLookingRight(eState);
									
									
									
									FloatByReference leftEyelid = new FloatByReference(0);
									FloatByReference rightEyelid = new FloatByReference(0);
									EmoState.INSTANCE.IS_FacialExpressionGetEyelidState(eState, leftEyelid, rightEyelid);
									Float.toString(leftEyelid.getValue());
									Float.toString(rightEyelid.getValue());
									
									
									
									FloatByReference leftEyeLoc = new FloatByReference(0);
									FloatByReference rightEyeLoc = new FloatByReference(0);
									EmoState.INSTANCE.IS_FacialExpressionGetEyeLocation(eState, leftEyeLoc,rightEyeLoc);
									Float.toString(leftEyeLoc.getValue());
									Float.toString(rightEyeLoc.getValue());
									
									
									
									
									Integer UpperFaceAction = EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceAction(eState);
									Float UpperFaceActionPow = EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceActionPower(eState);
										Integer Surprise = new Integer(0); Integer Frown = new Integer(0); 
										if(UpperFaceAction.intValue()==32){//Surprise
											Surprise = new Integer(1);
										}else if(UpperFaceAction.intValue()==64){//Frown
											Frown = new Integer(1);
										}
									
										
										
									Integer LowerFaceAction = EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceAction(eState);
									Float LowerFaceActionPow = EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceActionPower(eState);
										Integer Smile = new Integer(0); Integer Clench = new Integer(0); Integer Laugh = new Integer(0);
										if(LowerFaceAction.intValue()==128){
											Smile = new Integer(1);
										}else if(LowerFaceAction.intValue()==256){
											Clench = new Integer(1);
										}else if(LowerFaceAction.intValue()==512){
											Laugh = new Integer(1);
										}
									
										
										
										
										
									//********Contact Quality********************
									//*******************************************
										int AF3_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_AF3.getType());
										int T7_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_T7.getType());
										int Pz_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_Pz.getType());
										int T8_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_T8.getType());
										int AF4_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_AF4.getType());
										
									
								}//End If Emo State Updated
							}//End If Get Next Event State == ok
					}//End While
			}//End if

	}//End main

	
	public static boolean Check_To_See_If_Emotiv_ConnectionIsEstablished(){
		if(Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") == EdkErrorCode.EDK_OK.ToInt()){
			return true;
		}else
			return false;
	}
	
	
	
}//End Class
