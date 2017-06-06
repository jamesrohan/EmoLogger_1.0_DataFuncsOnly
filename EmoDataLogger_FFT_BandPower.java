
import com.emotiv.Iedk.*;
import com.emotiv.Iedk.EmoState.IEE_InputChannels_t;
import com.sun.jna.*;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.DoubleByReference;


public class EmoDataLogger_FFT_BandPower {

	static Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
	static Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();
	static FileWrite OutPutFile_Data = new FileWrite("FFT_DataLog",".csv");
	static int state =0; static IntByReference userID = new IntByReference(1);
	
	public static void main(String[] args) {
		if(Check_To_See_If_Emotiv_ConnectionIsEstablished() == true){
			OutPutFile_Data.CreatePrintWriter();
			OutPutFile_Data.Write_FFT_Header();
			
			while(true){
				state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);
				
				if (state == EdkErrorCode.EDK_OK.ToInt()){
					
					int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
					Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);
					
					if(eventType == Edk.IEE_Event_t.IEE_UserAdded.ToInt()){
						DoubleByReference alpha     = new DoubleByReference(0);
						DoubleByReference low_beta  = new DoubleByReference(0);
						DoubleByReference high_beta = new DoubleByReference(0);
						DoubleByReference gamma     = new DoubleByReference(0);
						DoubleByReference theta     = new DoubleByReference(0);
						
						
						
			
			            for(int i = 3 ; i < 17 ; i++)
			            {
			            	
			            	IntByReference chargeLevel = new IntByReference(0); IntByReference maxChargeLevel = new IntByReference(0);
							Float timestamp = EmoState.INSTANCE.IS_GetTimeFromStart(eState);
							EmoState.INSTANCE.IS_GetBatteryChargeLevel(eState, chargeLevel, maxChargeLevel);
							int SignalStrength = EmoState.INSTANCE.IS_GetWirelessSignalStatus(eState);
							int IS_GetHeadsetOn = EmoState.INSTANCE.IS_GetHeadsetOn(eState);
							
							
			                int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(), i, theta, alpha, low_beta, high_beta, gamma);
			                if(result == EdkErrorCode.EDK_OK.ToInt()){
			                	System.out.print(theta.getValue()); System.out.print(", ");
			                	System.out.print(alpha.getValue()); System.out.print(", ");
			                	System.out.print(low_beta.getValue()); System.out.print(", ");
			                	System.out.print(high_beta.getValue()); System.out.print(", ");
			                	System.out.print(gamma.getValue()); System.out.print(", ");	                	
			                }
			                
			                
			                
			              //********Contact Quality********************
							//*******************************************
								int AF3_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_AF3.getType());
								int T7_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_T7.getType());
								int Pz_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_Pz.getType());
								int T8_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_T8.getType());
								int AF4_Qual = EmoState.INSTANCE.IS_GetContactQuality(eState, IEE_InputChannels_t.IEE_CHAN_AF4.getType());
								
								
								IntByReference type = new IntByReference(0);
								Edk.INSTANCE.IEE_FFTGetWindowingType(userID.getValue(), type);
								
								double Defau_WindowSize = 2; double Defau_StepSize = 0.5;
			                
			                System.out.println();
			            }	//End For            	      
						
					}//End if Event Type == User ID
				}//End if Get Next Event state == ok
				
			}//End While
			
		}//End If Connection Established
		
	}//End Main
	
	
	
	
	public static boolean Check_To_See_If_Emotiv_ConnectionIsEstablished(){
		if(Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") == EdkErrorCode.EDK_OK.ToInt()){
			return true;
		}else
			return false;
	}

}//End FFT Class
